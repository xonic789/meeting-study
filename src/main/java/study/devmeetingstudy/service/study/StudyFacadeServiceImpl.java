package study.devmeetingstudy.service.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.devmeetingstudy.annotation.dto.MemberResolverDto;
import study.devmeetingstudy.common.uploader.Uploader;
import study.devmeetingstudy.domain.Subject;
import study.devmeetingstudy.domain.enums.DomainType;
import study.devmeetingstudy.domain.member.Member;
import study.devmeetingstudy.domain.study.*;
import study.devmeetingstudy.domain.study.enums.StudyAuth;
import study.devmeetingstudy.dto.study.CreatedStudyDto;
import study.devmeetingstudy.dto.study.StudyDto;
import study.devmeetingstudy.dto.study.request.StudyPutReqDto;
import study.devmeetingstudy.dto.study.request.StudySearchCondition;
import study.devmeetingstudy.service.AuthServiceImpl;
import study.devmeetingstudy.service.interfaces.StudyFacadeService;
import study.devmeetingstudy.vo.StudyReplaceVO;
import study.devmeetingstudy.vo.StudySaveVO;
import study.devmeetingstudy.dto.study.request.StudySaveReqDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyFacadeServiceImpl implements StudyFacadeService {

    private final StudyServiceImpl studyService;
    private final AddressServiceImpl addressService;
    private final OfflineServiceImpl offlineService;
    private final OnlineServiceImpl onlineService;
    private final StudyFileServiceImpl studyFileService;
    private final StudyMemberServiceImpl studyMemberService;
    private final SubjectServiceImpl subjectService;
    private final Uploader uploader;
    private final AuthServiceImpl authService;

    // TODO 파일이 비어있을시 기본 이미지 추가.
    @Transactional
    public CreatedStudyDto storeStudy(StudySaveReqDto studySaveReqDto, Member loginMember) throws IOException {
        Subject foundSubject = subjectService.getSubjectById(studySaveReqDto.getSubjectId());
        Map<String, String> uploadFileInfo =
                isFileExists(studySaveReqDto.getFile()) ?
                        uploader.upload(studySaveReqDto.getFile(), DomainType.STUDY) : studyFileService.getDefaultFile(foundSubject.getName());
        Study createdStudy = studyService.saveStudy(StudySaveVO.of(studySaveReqDto, foundSubject));
        StudyFile studyFile = studyFileService.saveStudyFile(createdStudy, uploadFileInfo);
        StudyMember studyMember = studyMemberService.saveStudyLeader(loginMember, createdStudy);
        return CreatedStudyDto.builder()
                .study(createdStudy)
                .studyFile(studyFile)
                .online(createdStudy.isDtypeOnline() ? onlineService.saveOnline(studySaveReqDto, createdStudy) : null)
                .offline(!createdStudy.isDtypeOnline() ? offlineService.saveOffline(addressService.getAddress(studySaveReqDto.getAddressId()), createdStudy) : null)
                .studyMember(studyMember)
                .build();
    }

    public List<StudyDto> getStudiesBySearchCondition(StudySearchCondition studySearchCondition) {
        List<Study> studies = studyService.getStudiesByStudySearchCondition(studySearchCondition);
        return studies.stream().map(
                study -> StudyDto.of(study,
                                studyMemberService.getStudyMemberByStudyIdAndStudyAuth(study.getId(), StudyAuth.LEADER),
                                studyFileService.getStudyFileByStudyId(study.getId()))
        ).collect(Collectors.toList());
    }

    public StudyDto getStudyByStudyId(Long studyId) {
        Study study = studyService.getStudyFetchJoinById(studyId);
        return StudyDto.of(study,
                studyMemberService.getStudyMembersByStudyId(studyId),
                studyFileService.getStudyFileByStudyId(study.getId()));
    }

    @Transactional
    public CreatedStudyDto replaceStudy(StudyPutReqDto studyPutReqDto, MemberResolverDto memberResolverDto) throws IOException {
        Long studyId = studyPutReqDto.getStudyId();
        Study foundStudy = studyService.getStudyById(studyId);
        // 에러 발생 Leader 존재하지 않음.
        StudyMember studyMember = checkStudyMemberLeader(studyId, memberResolverDto);
        Subject foundSubject = subjectService.getSubjectById(studyPutReqDto.getSubjectId());
        Object onlineOrOffline = SyncOrReplaceOnlineOrOffline(studyPutReqDto, foundStudy);

        return CreatedStudyDto.builder()
                .study(studyService.replaceStudy(StudyReplaceVO.of(studyPutReqDto, foundSubject), foundStudy))
                .online(onlineOrOffline instanceof Online ? (Online) onlineOrOffline : null)
                .offline(onlineOrOffline instanceof Offline ? (Offline) onlineOrOffline : null)
                .studyMember(studyMember)
                .studyFile(replaceStudyFile(studyPutReqDto))
                .build();
    }

    private StudyMember checkStudyMemberLeader(Long studyId, MemberResolverDto memberResolverDto) {
        StudyMember studyMember = studyMemberService.getStudyLeaderByStudyId(studyId);
        authService.checkUserInfo(studyMember.getMember().getId(), memberResolverDto);
        return studyMember;
    }

    private Object SyncOrReplaceOnlineOrOffline(StudyPutReqDto studyPutReqDto, Study foundStudy) {
        if (!foundStudy.getDtype().equals(studyPutReqDto.getDtype())) {
            return syncOnlineOrOffline(studyPutReqDto, foundStudy);
        }
        return replaceOnlineOrOffline(studyPutReqDto);
    }

    private Object syncOnlineOrOffline(StudyPutReqDto studyPutReqDto, Study foundStudy) {
        if (studyPutReqDto.getDtype().isOnline()) {
            return deleteOfflineAndSaveOnline(studyPutReqDto, foundStudy);
        }
        return deleteOnlineAndSaveOffline(studyPutReqDto, foundStudy);
    }

    private Online deleteOfflineAndSaveOnline(StudyPutReqDto studyPutReqDto, Study foundStudy) {
        foundStudy.removeOffline();
        offlineService.deleteOffline(offlineService.getOfflineByStudyId(studyPutReqDto.getStudyId()));
        return onlineService.saveOnline(StudySaveReqDto.of(studyPutReqDto), foundStudy);
    }

    private Offline deleteOnlineAndSaveOffline(StudyPutReqDto studyPutReqDto, Study foundStudy) {
        foundStudy.removeOnline();
        onlineService.deleteOnline(onlineService.getOnlineByStudyId(studyPutReqDto.getStudyId()));
        return offlineService.saveOffline(addressService.getAddress(studyPutReqDto.getAddressId()), foundStudy);
    }

    private Object replaceOnlineOrOffline(StudyPutReqDto studyPutReqDto) {
        if (studyPutReqDto.getDtype().isOnline()) {
            return onlineService.replaceOnline(studyPutReqDto, onlineService.getOnlineById(studyPutReqDto.getOnlineId()));
        }
        return offlineService.replaceOffline(
                addressService.getAddress(studyPutReqDto.getAddressId()),
                offlineService.getOfflineById(studyPutReqDto.getOfflineId()));
    }

    private StudyFile replaceStudyFile(StudyPutReqDto studyPutReqDto) throws IOException {
        if (isFileExists(studyPutReqDto.getFile())) {
            Map<String, String> upload = uploader.upload(studyPutReqDto.getFile(), DomainType.STUDY);
            StudyFile studyFile = studyFileService.getStudyFileById(studyPutReqDto.getStudyFileId());
            return studyFileService.replaceStudyFile(upload, studyFile);
        }
        return studyFileService.getStudyFileById(studyPutReqDto.getStudyFileId());
    }   

    private boolean isFileExists(MultipartFile multipartFile) {
        return multipartFile != null && !multipartFile.isEmpty();
    }

    @Transactional
    public void deleteStudy(Long studyId, MemberResolverDto memberResolverDto) {
        Study foundStudy = studyService.getStudyById(studyId);
        checkStudyMemberLeader(studyId, memberResolverDto);
        studyService.deleteStudyById(foundStudy);
    }

    public List<StudyDto> getStudiesByMemberId(Long memberId) {
        List<StudyMember> studyMembers = studyMemberService.getStudyMembersByMemberId(memberId);
        return studyMembers.stream().map(studyMember ->
            StudyDto.of(
                    studyMember.getStudy(),
                    studyMemberService.getStudyMemberByStudyIdAndStudyAuth(studyMember.getStudy().getId(), StudyAuth.LEADER),
                    studyFileService.getStudyFileByStudyId(studyMember.getStudy().getId()))
        ).collect(Collectors.toList());
    }
}
