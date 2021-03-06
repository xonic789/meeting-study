package study.devmeetingstudy.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import study.devmeetingstudy.domain.member.Member;
import study.devmeetingstudy.domain.member.enums.Authority;
import study.devmeetingstudy.domain.member.enums.MemberStatus;
import study.devmeetingstudy.service.AuthServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class AuthControllerTest {


    private MockMvc mvc;

    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthServiceImpl authService;

    private List<Member> memberList;

    @BeforeEach
    void setUp(){
        this.memberList = new ArrayList<>();
        this.memberList.add(buildMember("test1@naver.com", "1234", "맴버1"));
        this.memberList.add(buildMember("test2@naver.com", "1234", "맴버2"));
        this.memberList.add(buildMember("test3@naver.com", "1234", "맴버3"));
    }

    private Member buildMember(String email, String password, String nickname) {
        return Member.builder()
                .id(1L)
                .email(email)
                .password(password)
                .authority(Authority.ROLE_USER)
                .grade(0)
                .status(MemberStatus.ACTIVE)
                .nickname(nickname)
                .build();
    }


    @Test
    public void signupTest() throws Exception{


//        given(authService.signup(any())).willReturn(buildMember("teats@naver.com","test","tsds"));


//        mvc.perform(MockMvcRequestBuilders
//                .post("/api/auth/signup")
//                .content()
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("test").exists());
    }
}