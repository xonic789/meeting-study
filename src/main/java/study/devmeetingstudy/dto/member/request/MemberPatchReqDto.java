package study.devmeetingstudy.dto.member.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MemberPatchReqDto {

    @NotBlank(message = "닉네임은 필수 입니다.")
    @Size(min = 2, max = 30, message = "닉네임은 최소 2 ~ 30자 이여야됩니다.")
    @ApiModelProperty(value = "닉네임", required = true, example = "테스트닉네임")
    private String nickname;

}
