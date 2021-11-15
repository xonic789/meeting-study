package study.devmeetingstudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.devmeetingstudy.annotation.dto.MemberResolverDto;
import study.devmeetingstudy.common.exception.global.error.exception.UserException;
import study.devmeetingstudy.domain.member.Member;
import study.devmeetingstudy.domain.member.enums.MemberStatus;
import study.devmeetingstudy.dto.member.request.MemberPatchReqDto;
import study.devmeetingstudy.repository.MemberRepository;
import study.devmeetingstudy.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getMemberInfo(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("유저 정보가 없습니다."));
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public Member getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UserException("로그인 유저 정보가 없습니다."));
    }

    public void deleteMember(MemberResolverDto dto){
       Member findMember = getMemberOne(dto.getId());
       findMember.changeStatus(MemberStatus.OUT);
    }

    public Member getMemberOne(Long id){

        return memberRepository.findById(id)
                .orElseThrow(() -> new UserException("로그인 유저 정보가 없습니다."));
    }

    // 일단 닉네임만 변경...
    @Transactional
    public Member changeMemberInfo(MemberPatchReqDto memberPatchReqDto, MemberResolverDto memberResolverDto) {
        Member foundMember = getMemberOne(memberResolverDto.getId());
        return foundMember.changeMember(memberPatchReqDto);
    }
}
