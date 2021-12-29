package study.devmeetingstudy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import study.devmeetingstudy.domain.Email;
import study.devmeetingstudy.dto.email.EmailVerifyCodeReqDto;
import study.devmeetingstudy.repository.email.EmailRepository;
import study.devmeetingstudy.service.interfaces.EmailService;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final JavaMailSenderImpl javaMailSender;
    private final EmailRepository emailRepository;

    private MimeMessage createMessage(String to)throws Exception{
        String key = createKey();
        log.info("보내는 대상 : {}", to);
        log.info("인증 번호 : {}", key);
        log.info("id : {} password : {}", javaMailSender.getUsername(), javaMailSender.getPassword());
        MimeMessage  message = emailSender.createMimeMessage();

        String code = createCode(createKey());
        Email createEmail = Email.createEmail(to, code);

        emailRepository.save(createEmail);

        message.addRecipients(Message.RecipientType.TO, to); //보내는 대상
        message.setSubject("dev-meeting-study 이메일 인증 코드입니다. "); //제목

        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 페이지에 입력해주세요. </p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += code;
        msg += "</td></tr></tbody></table></div>";
        msg += "<a href=\"https://slack.com\" style=\"text-decoration: none; color: #434245;\" rel=\"noreferrer noopener\" target=\"_blank\">Slack Clone Technologies, Inc</a>";

        message.setText(msg, "utf-8", "html"); //내용
        message.setFrom(new InternetAddress(javaMailSender.getUsername(),"dev-meeting-study")); //보내는 사람

        return message;
    }

    // 인증코드 만들기
    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    public void sendSimpleMessage(String to)throws Exception {
        MimeMessage message = createMessage(to);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public String createCode(String ePw){
        return ePw.substring(0, 3) + "-" + ePw.substring(3, 6);
    }


    public boolean emailCheck(EmailVerifyCodeReqDto dto) {

        Optional<Email> byEmail = emailRepository.findByEmail(dto.getEmail());

        return byEmail.filter(email -> dto.getAuth_number().equals(
                email.getAuthNumber())).isPresent();
    }
}
