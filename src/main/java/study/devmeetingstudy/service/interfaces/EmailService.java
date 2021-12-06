package study.devmeetingstudy.service.interfaces;

import study.devmeetingstudy.dto.email.EmailVerifyCodeReqDto;

public interface EmailService {

    String createKey();

    void sendSimpleMessage(String to) throws Exception;

    String createCode(String ePw);

    boolean emailCheck(EmailVerifyCodeReqDto dto);
}
