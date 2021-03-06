package study.devmeetingstudy.dto.member.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignupReqDto {

    private final static int GRADE = 0;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    @Size(min = 10, max = 100, message = "이메일은 최소 10 ~ 100자 이여야됩니다.")
    @ApiModelProperty(value = "이메일", required = true, example = "test@naver.com")
    private String email;

    @NotBlank(message = "닉네임은 필수 입니다.")
    @Size(min = 2, max = 30, message = "닉네임은 최소 2 ~ 30자 이여야됩니다.")
    @ApiModelProperty(value = "닉네임", required = true, example = "테스트닉네임")
    private String nickname;

    @NotBlank(message = "비밀번호은 필수입니다.")
    @ApiModelProperty(value = "비밀번호", required = true, example = "123456")
    private String password;

}