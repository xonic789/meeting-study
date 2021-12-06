package study.devmeetingstudy.service.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.devmeetingstudy.common.exception.global.error.exception.notfound.StudyNotFoundException;
import study.devmeetingstudy.domain.enums.DeletionStatus;
import study.devmeetingstudy.domain.study.Study;
import study.devmeetingstudy.dto.study.request.StudySearchCondition;
import study.devmeetingstudy.service.interfaces.StudyService;
import study.devmeetingstudy.vo.StudyReplaceVO;
import study.devmeetingstudy.vo.StudySaveVO;
import study.devmeetingstudy.repository.study.StudyRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final StudyRepository studyRepository;
    private final AddressServiceImpl addressService;


    @Transactional
    public Study saveStudy(StudySaveVO studySaveVO) {
        return studyRepository.save(Study.create(studySaveVO.getStudySaveReqDto(), studySaveVO.getSubject()));
    }

    public List<Study> getStudiesByStudySearchCondition(StudySearchCondition studySearchCondition) {
        return studyRepository.findStudiesByStudySearchCondition(studySearchCondition);
    }

    public Study getStudyFetchJoinById(Long studyId) {
        return studyRepository.findStudyById(studyId).orElseThrow(() -> new StudyNotFoundException("스터디를 찾을 수 없습니다."));
    }

    public Study getStudyById(Long studyId) {
        return studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("스터디를 찾을 수 없습니다"));
    }

    public boolean existsStudyByStudyId(Long studyId) {
        return studyRepository.existsStudyById(studyId);
    }

    @Transactional
    public Study replaceStudy(StudyReplaceVO studyReplaceVO, Study foundStudy) {
        return Study.replace(studyReplaceVO, foundStudy);
    }

    @Transactional
    public void deleteStudyById(Study foundStudy) {
        if (foundStudy.isNotDeleted()) {
            foundStudy.changeDeletionStatus(DeletionStatus.DELETED);
        }
    }

}
