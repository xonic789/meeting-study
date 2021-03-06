package study.devmeetingstudy.service;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import study.devmeetingstudy.common.exception.global.error.exception.notfound.MessageNotFoundException;
import study.devmeetingstudy.domain.member.Member;
import study.devmeetingstudy.domain.member.enums.Authority;
import study.devmeetingstudy.domain.member.enums.MemberStatus;
import study.devmeetingstudy.domain.message.Message;
import study.devmeetingstudy.domain.enums.DeletionStatus;
import study.devmeetingstudy.domain.message.enums.MessageReadStatus;
import study.devmeetingstudy.dto.message.MessageReqDto;
import study.devmeetingstudy.repository.message.MessageRepository;
import study.devmeetingstudy.service.interfaces.MessageService;
import study.devmeetingstudy.vo.MessageVO;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    private Member member;
    private Member loginMember;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Mock
    private MessageRepository messageRepository;


    @BeforeEach
    void init(){
        loginMember = createMember(1L, "dltmddn@na.na", "nick1");
        member = createMember(2L, "xonic@na.na", "nick2");
    }

    private Member createMember(Long id, String email, String nickname){
        return Member.builder()
                .authority(Authority.ROLE_USER)
                .email(email)
                .password("123456")
                .status(MemberStatus.ACTIVE)
                .grade(0)
                .nickname(nickname)
                .id(id)
                .build();
    }


    @DisplayName("????????? ??????")
    @Test
    void send() throws Exception{
        //given
        // ????????? ??? ????????? ???????????? ????????? ??????

        MessageReqDto messageReqDto = new MessageReqDto("dltmddn@na.na", "??????");
        MessageVO messageVO = MessageVO.of(messageReqDto, member, loginMember);

        // messageRepository.save??? ????????? ?????????
        Message createdMessage = createMessage(1L, member, loginMember);
        // messageRepository mocking
        doReturn(createdMessage).when(messageRepository).save(any(Message.class));

        //when
        Message message = messageService.sendMessage(messageVO);

        //then
        assertNotNull(message);
    }

    private Message createMessage(Long id, Member member, Member sender) {
        return Message.builder()
                .id(id)
                .content("??????")
                .senderName(sender.getNickname())
                .senderId(sender.getId())
                .member(member)
                .deletionStatus(DeletionStatus.NOT_DELETED)
                .status(MessageReadStatus.NOT_READ).build();
    }

    @DisplayName("????????? ??????")
    @Test
    void getMessage() throws Exception{
        //given
        Long messageId = 1L;
        Message createdMessage = createMessage(messageId, loginMember, member);

        doReturn(Optional.of(createdMessage)).when(messageRepository).findById(messageId);

        //when
        Message message = messageService.getMessage(messageId);

        //then
        assertEquals(MessageReadStatus.READ, message.getStatus());
    }

    @DisplayName("????????? ?????? MessageNotFoundException")
    @Test
    void getMessage_messageNotFoundException() throws Exception{
        //given
        Long messageId = 1L;

        doReturn(Optional.empty()).when(messageRepository).findById(messageId);

        //when
        MessageNotFoundException messageNotFoundException = assertThrows(MessageNotFoundException.class, () -> messageService.getMessage(messageId));
        String message = messageNotFoundException.getMessage();

        //then
        assertEquals("?????? id??? ???????????? ?????? ??? ????????????.", message);
    }

    @DisplayName("????????? ??????")
    @Test
    void getMessages() throws Exception{
        //given
        List<Message> createdMessages = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            createdMessages.add(Message.create(MessageVO.of(new MessageReqDto("??????", member.getEmail()), loginMember, member)));
        }
        doReturn(createdMessages).when(messageRepository).findMessagesDesc(loginMember);

        //when
        List<Message> messages = messageService.getMessages(loginMember);

        //then
        assertEquals(5, messages.size());
    }

    @DisplayName("????????? ??????")
    @Test
    void deleteMessage() throws Exception{
        //given
        Long messageId = 1L;
        Message notDeletedMessage = createMessage(1L, loginMember, member);
        Message deletedMessage = createMessage(1L, loginMember, member);
        deletedMessage.changeDeletionStatus(DeletionStatus.DELETED);

        doReturn(deletedMessage).when(messageRepository).save(any(Message.class));
        //when

        messageService.deleteMessage(notDeletedMessage);

        //then
        assertEquals(DeletionStatus.DELETED, deletedMessage.getDeletionStatus());
    }
}