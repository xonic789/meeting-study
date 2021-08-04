package study.devmeetingstudy.common.exception.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApiResponseDto {

    private String message;
    private int status;
    private Object object;
}