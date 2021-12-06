package study.devmeetingstudy.service.interfaces;

import org.springframework.transaction.annotation.Transactional;
import study.devmeetingstudy.annotation.dto.MemberResolverDto;
import study.devmeetingstudy.common.exception.global.error.exception.BusinessException;
import study.devmeetingstudy.common.exception.global.error.exception.ErrorCode;
import study.devmeetingstudy.common.exception.global.error.exception.ExistsStudyMemberException;
import study.devmeetingstudy.common.exception.global.error.exception.UnableApplyStudyMemberException;
import study.devmeetingstudy.common.exception.global.error.exception.notfound.StudyMemberNotFoundException;
import study.devmeetingstudy.domain.member.Member;
import study.devmeetingstudy.domain.study.Study;
import study.devmeetingstudy.domain.study.StudyMember;
import study.devmeetingstudy.domain.study.enums.StudyAuth;
import study.devmeetingstudy.domain.study.enums.StudyMemberStatus;

import java.util.List;

public interface StudyMemberService {

    StudyMember saveStudyMember(Member member, Study study);

    StudyMember saveStudyLeader(Member member, Study study);

    List<StudyMember> getStudyMemberByStudyIdAndStudyAuth(Long studyId, StudyAuth studyAuth);

    StudyMember getStudyLeaderByStudyId(Long studyId);

    List<StudyMember> getStudyMembersByStudyId(Long studyId);

    void authenticateStudyMember(Long studyId, MemberResolverDto memberResolverDto);

    boolean existsStudyMemberByStudyIdAndMemberId(Long studyId, Long memberId);

    boolean checkStudyMemberStatusJOIN(Long studyId, Long memberId);

    StudyMember getStudyMemberById(Long studyMemberId);

    int countStudyMemberByStudyId(Long studyId);

    void deleteStudyMember(Long studyMemberId, MemberResolverDto memberResolverDto);

    List<StudyMember> getStudyMembersByMemberId(Long memberId);
}
