package study.devmeetingstudy.service.interfaces;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import study.devmeetingstudy.annotation.dto.MemberResolverDto;
import study.devmeetingstudy.common.exception.global.error.exception.*;
import study.devmeetingstudy.domain.RefreshToken;
import study.devmeetingstudy.domain.member.Member;
import study.devmeetingstudy.domain.member.enums.MemberStatus;
import study.devmeetingstudy.dto.member.request.MemberLoginReqDto;
import study.devmeetingstudy.dto.member.request.MemberSignupReqDto;
import study.devmeetingstudy.dto.token.TokenDto;
import study.devmeetingstudy.dto.token.request.TokenReqDto;

import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    Member signup(MemberSignupReqDto memberRequestDto);

    TokenDto login(MemberLoginReqDto memberRequestDto, HttpServletResponse response);

    TokenDto reissue(TokenReqDto tokenReqDto);

    void checkUserInfo(Long id, MemberResolverDto dto);

    boolean isExistsNickname(String nickname);

    void checkDuplicateNickname(String nickname);
}
