package study.devmeetingstudy.service.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import study.devmeetingstudy.common.exception.global.error.exception.MessageNotFoundException;
import study.devmeetingstudy.domain.Authority;
import study.devmeetingstudy.domain.Member;
import study.devmeetingstudy.domain.Message;
import study.devmeetingstudy.domain.UserStatus;
import study.devmeetingstudy.dto.message.MessageRequestDto;
import study.devmeetingstudy.repository.MemberRepository;
import study.devmeetingstudy.repository.message.MessageRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MessageServiceTest {

    @PersistenceContext EntityManager em;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessageService messageService;
    @Autowired
    MemberRepository memberRepository;
    Member member;
    Member sender;

    @BeforeEach
    void setMemberAndSender(){
        member = buildMember("xonic@na.na","1234");
        sender = buildMember("dltmddn@na.na", "1234");
        em.persist(member);
        em.persist(sender);
        em.flush();
        em.clear();
    }

    private Member buildMember(String email, String password) {
        return Member.builder()
                .email(email)
                .password(password)
                .authority(Authority.ROLE_ADMIN)
                .grade(0)
                .status(UserStatus.active).build();
    }

    private Message buildMessage(Member member, Member sender){
        return Message.builder()
                .senderId(sender.getId())
                .content("1234")
                .senderName(sender.getEmail())
                .member(member).build();
    }


    @Test
    void 메시지생성_객체가같은지_True(){
        // given

        Message memberToSenderMessage = messageService.save(getMessageDto(member,sender));

        // when
        em.flush();
        em.clear();

        // then
        assertEquals(memberToSenderMessage,messageService.getMessage(memberToSenderMessage.getId()));
    }

    private MessageRequestDto getMessageDto(Member member, Member sender){
        if (this.member.equals(member)) {
            member = this.member;
            sender = this.sender;
        }
        else {
            member = this.sender;
            sender = this.member;
        }
        return createMessage(member,sender);
    }

    private MessageRequestDto createMessage(Member member, Member sender){
        return MessageRequestDto.builder()
                .member(member)
                .content("하이")
                .sender(sender)
                .build();
    }

    @Test
    void 메시지찾기_엑셉션발생(){
        //given
        //when

        //then
        assertThrows(MessageNotFoundException.class, () -> messageService.getMessage(1L));
    }

    @Test
    public void 메시지다가져오기_사이즈비교_True() throws Exception{
        //given
        for (int i = 0; i < 5; i++){
            messageService.save(getMessageDto(member,sender));
        }

        //when
        List<Message> messages = messageService.getMessages(member);
        //then
        assertEquals(5,messages.size());
    }
}