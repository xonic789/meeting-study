package study.devmeetingstudy.common;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.devmeetingstudy.domain.Subject;
import study.devmeetingstudy.domain.member.Member;
import study.devmeetingstudy.dto.member.request.MemberSignupReqDto;
import study.devmeetingstudy.dto.subject.SubjectReqDto;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Component
public class DBInitializer implements ApplicationRunner {

  private final EntityManager em;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(ApplicationArguments args) throws Exception {
    String[] subjects = {"Java", "C#", "C++", "Javascript", "Kotlin", "Python", "Spring"};
    for (String s : subjects) {
      Subject subject = Subject.create(new SubjectReqDto(s));
      em.persist(subject);
    }
//    MemberSignupReqDto testUser = new MemberSignupReqDto("test@naver.com", "testUser", "123456");
//    Member member = Member.create(testUser, passwordEncoder);
//    em.persist(member);
  }
}
