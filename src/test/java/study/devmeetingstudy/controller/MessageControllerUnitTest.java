package study.devmeetingstudy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import study.devmeetingstudy.annotation.dto.MemberResolverDto;
import study.devmeetingstudy.annotation.handlerMethod.MemberDecodeResolver;
import study.devmeetingstudy.common.exception.global.error.CustomGlobalExceptionHandler;
import study.devmeetingstudy.common.exception.global.error.exception.UserInfoMismatchException;
import study.devmeetingstudy.domain.member.Member;
import study.devmeetingstudy.domain.member.enums.Authority;
import study.devmeetingstudy.domain.member.enums.MemberStatus;
import study.devmeetingstudy.domain.message.Message;
import study.devmeetingstudy.domain.enums.DeletionStatus;
import study.devmeetingstudy.domain.message.enums.MessageReadStatus;
import study.devmeetingstudy.dto.message.MessageReqDto;
import study.devmeetingstudy.service.AuthServiceImpl;
import study.devmeetingstudy.service.interfaces.AuthService;
import study.devmeetingstudy.service.interfaces.MemberService;
import study.devmeetingstudy.service.interfaces.MessageService;
import study.devmeetingstudy.vo.MessageVO;
import study.devmeetingstudy.dto.token.TokenDto;
import study.devmeetingstudy.jwt.TokenProvider;
import study.devmeetingstudy.repository.MemberRepository;
import study.devmeetingstudy.service.MemberServiceImpl;
import study.devmeetingstudy.service.MessageServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// ????????? Mockito ??????
@ExtendWith(MockitoExtension.class)
class MessageControllerUnitTest {
    /** TODO: ?????? ?????? ?????? ?????? ????????? ????????? ?????? ?????? ???????
     *      1. ????????? ?????????
     *      2. ????????? ??????
     *          -
     *      3. ????????? ??????
 *              - ????????? ?????? ?????? ??????
     *      4. ????????? ?????? ?????? ??????
     *
     */

    @InjectMocks
    private MessageController messageApiController;

    @Mock
    private MessageService messageService;

    @Mock
    private MemberService memberService;

    //ArgumentResolver ?????? ?????????
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;

    private Member loginMember;

    private Member member;

    private TokenProvider tokenProvider;

    @BeforeEach
    void init(){
        // ?????? ArgumentResolver ??????.
        tokenProvider = new TokenProvider("c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK");
        mockMvc = MockMvcBuilders.standaloneSetup(messageApiController)
                .addFilter((((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                })))
                .setCustomArgumentResolvers(new MemberDecodeResolver(tokenProvider, memberRepository))
                .setControllerAdvice(new CustomGlobalExceptionHandler())
                .build();
        // ???????????? ?????? ??????
        member = createMember(1L, "dltmddn@na.na", "nick1");
        // ???????????? ????????? ??????
        loginMember = createMember(2L, "xonic@na.na", "nick2");
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

    @DisplayName("????????? ????????? 201 created")
    @Test
    void sendMessage() throws Exception{
        //given
        // ?????? ?????? ??? ??????
        // ?????? ????????? ??? ????????? sender ??????.
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(loginMember.getAuthority().toString());
        Authentication token = new UsernamePasswordAuthenticationToken(loginMember.getId(), loginMember.getPassword(), Collections.singleton(grantedAuthority));
        // sender ???????????? token ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(token);

        MessageReqDto messageReqDto = new MessageReqDto("dltmddn@na.na", "Hello");

        // mock ????????? ????????? ?????? ???????????? ?????? ??????.
        Message message = createMessage(1L, member, loginMember);
        // controller?????? ????????? getUserOne ??????
        // sender userOne
        // member memberInfo
        doReturn(Optional.of(loginMember)).when(memberRepository).findById(anyLong());
        doReturn(loginMember).when(memberService).getMemberOne(any(Long.class));
        doReturn(member).when(memberService).getMemberInfo(any(String.class));
        // ?????? ????????? ???????????? ?????????.
        doReturn(message).when(messageService).sendMessage(any(MessageVO.class));

        //when
        // ?????? ???????????? ??? ??????
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","bearer " + tokenDto.getAccessToken())
                        .content(new ObjectMapper().writeValueAsString(messageReqDto)));

        //then
        final MvcResult mvcResult = resultActions.andExpect(status().isCreated()).andReturn();
        String body = mvcResult.getResponse().getContentAsString();
        // jackson?????? ??? No Argument Constructor ??? ???????????? ????????????.
        JSONObject data = (JSONObject) getDataOfJSON(body);
        Long id = (Long) data.get("id");
        assertEquals(1L, id);
    }

    private Object getDataOfJSON(String body) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(body);
        return json.get("data");
    }

    private Message createMessage(Long id, Member member, Member sender) {
        return Message.builder()
                .id(id)
                .content("??????")
                .senderName(sender.getNickname())
                .senderId(sender.getId())
                .member(member)
                .deletionStatus(DeletionStatus.NOT_DELETED)
                .status(MessageReadStatus.NOT_READ).createdDate(LocalDateTime.now()).lastUpdateDate(LocalDateTime.now()).build();
    }

    @DisplayName("????????? ?????? 200 Ok")
    @Test
    void showMessages() throws Exception {
        // ????????? 5??? ??????
        //given
        List<Message> messages = new ArrayList<>();
        for (long i = 1; i <= 5; i++){
            messages.add(createMessage(i, member, loginMember));
        }
        // ?????? ?????? ??? ??????
        // ?????? ????????? ??? ????????? member ??????.
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());
        Authentication token = new UsernamePasswordAuthenticationToken(member.getId(), member.getPassword(), Collections.singleton(grantedAuthority));
        // member ???????????? token ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(token);
        doReturn(Optional.of(loginMember)).when(memberRepository).findById(anyLong());
        doReturn(member).doReturn(loginMember).doReturn(loginMember).doReturn(loginMember).doReturn(loginMember).doReturn(loginMember).when(memberService).getMemberOne(any(Long.class));
        doReturn(messages).when(messageService).getMessages(any(Member.class));

        //when
        // ?????? ???????????? ??? ??????
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","bearer " + tokenDto.getAccessToken()));
        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        String body = mvcResult.getResponse().getContentAsString();
        JSONArray data = (JSONArray) getDataOfJSON(body);

        assertEquals(5, data.size());
    }


    @DisplayName("????????? ?????? 200 Ok")
    @Test
    void showMessage() throws Exception{
        //given
        // ????????? ??????
        Message createdMessage = createMessage(1L, member, loginMember);

        //?????? ??? ????????? ?????? ?????? ??????
        Message readMessage = createdMessage.changeReadStatus(MessageReadStatus.READ);
        // ?????? ?????? ??? ??????
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(loginMember.getAuthority().toString());
        Authentication token = new UsernamePasswordAuthenticationToken(loginMember.getId(), loginMember.getPassword(), Collections.singleton(grantedAuthority));
        // loginMember ???????????? token ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(token);
        doReturn(Optional.of(loginMember)).when(memberRepository).findById(anyLong());
        doReturn(loginMember).doReturn(member).when(memberService).getMemberOne(anyLong());
        doReturn(readMessage).when(messageService).getMessage(anyLong());
        doNothing().when(authService).checkUserInfo(anyLong(), any(MemberResolverDto.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/messages/" + createdMessage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "bearer " + tokenDto.getAccessToken()));

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        String body = mvcResult.getResponse().getContentAsString();
        JSONObject data = (JSONObject) getDataOfJSON(body);

        assertEquals(MessageReadStatus.READ.toString(), data.get("status"));
    }

    @DisplayName("????????? ?????? 404 Not Found")
    @Test
    void showMessage_UserInfoNotFoundException() throws Exception{
        //given
        // ????????? ??????
        Message createdMessage = createMessage(1L, loginMember, member);

        //?????? ??? ????????? ?????? ?????? ??????
        Message readMessage = createdMessage.changeReadStatus(MessageReadStatus.READ);
        // ?????? ?????? ??? ??????
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(loginMember.getAuthority().toString());
        Authentication token = new UsernamePasswordAuthenticationToken(loginMember.getId(), loginMember.getPassword(), Collections.singleton(grantedAuthority));
        // loginMember ???????????? token ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(token);

        doReturn(Optional.of(loginMember)).when(memberRepository).findById(anyLong());

        doReturn(loginMember).doReturn(member).when(memberService).getMemberOne(anyLong());

        doReturn(readMessage).when(messageService).getMessage(anyLong());

        doThrow(new UserInfoMismatchException("?????? ????????? ???????????? ????????????.")).when(authService).checkUserInfo(anyLong(), any(MemberResolverDto.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/messages/" + createdMessage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "bearer " + tokenDto.getAccessToken()));

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isForbidden()).andReturn();
        String body = mvcResult.getResponse().getContentAsString();
        System.out.println(body);
    }

    @DisplayName("????????? ?????? 204 No Content")
    @Test
    void deleteMessage() throws Exception{
        //given
        // ????????? ??????
        Message createdMessage = createMessage(1L, loginMember, member);
        //?????? ??? ????????? ?????? ?????? ??????
        Message deletedMessage = createdMessage.changeDeletionStatus(DeletionStatus.DELETED);
        // ?????? ?????? ??? ??????
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(loginMember.getAuthority().toString());
        Authentication token = new UsernamePasswordAuthenticationToken(loginMember.getId(), loginMember.getPassword(), Collections.singleton(grantedAuthority));
        // loginMember ???????????? token ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(token);
        doReturn(Optional.of(loginMember)).when(memberRepository).findById(anyLong());
        doReturn(createdMessage).when(messageService).getMessage(anyLong());
        doNothing().when(authService).checkUserInfo(anyLong(), any(MemberResolverDto.class));
        doNothing().when(messageService).deleteMessage(any(Message.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/messages/" + createdMessage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "bearer " + tokenDto.getAccessToken()));

        //then
        resultActions.andExpect(status().isNoContent()).andReturn();
    }

    @DisplayName("????????? ?????? 404 Not Found")
    @Test
    void deleteMessage_UserInfoNotFoundException() throws Exception{
        //given
        // ????????? ??????
        Message createdMessage = createMessage(1L, member, loginMember);
        //?????? ??? ????????? ?????? ?????? ??????
        Message deletedMessage = createdMessage.changeDeletionStatus(DeletionStatus.DELETED);
        // ?????? ?????? ??? ??????
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(loginMember.getAuthority().toString());
        Authentication token = new UsernamePasswordAuthenticationToken(loginMember.getId(), loginMember.getPassword(), Collections.singleton(grantedAuthority));
        // loginMember ???????????? token ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(token);
        // sender ???????????? ????????????.
        doReturn(Optional.of(loginMember)).when(memberRepository).findById(anyLong());
        doReturn(createdMessage).when(messageService).getMessage(anyLong());
        doThrow(new UserInfoMismatchException("?????? ????????? ???????????? ????????????.")).when(authService).checkUserInfo(anyLong(), any(MemberResolverDto.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/messages/" + createdMessage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "bearer " + tokenDto.getAccessToken()));

        //then
        resultActions.andExpect(status().isForbidden()).andReturn();
    }

}