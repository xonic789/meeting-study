package study.devmeetingstudy.service.interfaces;

import org.springframework.transaction.annotation.Transactional;
import study.devmeetingstudy.annotation.dto.MemberResolverDto;
import study.devmeetingstudy.common.exception.global.error.exception.UserException;
import study.devmeetingstudy.domain.member.Member;
import study.devmeetingstudy.domain.member.enums.MemberStatus;
import study.devmeetingstudy.dto.member.request.MemberPatchReqDto;
import study.devmeetingstudy.util.SecurityUtil;

public interface MemberService {

    Member getMemberInfo(String email);

    Member getMyInfo();

    void deleteMember(MemberResolverDto dto);

    Member getMemberOne(Long id);

    Member changeMemberInfo(MemberPatchReqDto memberPatchReqDto, MemberResolverDto memberResolverDto);
}
