package study.devmeetingstudy.domain.member;


import io.jsonwebtoken.lang.Assert;
import lombok.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.devmeetingstudy.domain.base.BaseTimeEntity;
import study.devmeetingstudy.domain.member.enums.Authority;
import study.devmeetingstudy.domain.member.enums.MemberStatus;
import study.devmeetingstudy.domain.message.Message;
import study.devmeetingstudy.dto.member.request.MemberPatchReqDto;
import study.devmeetingstudy.dto.member.request.MemberSignupReqDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Table(name = "member")
@Entity
@ToString(exclude = {"messages"})
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 30, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private int grade;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;


    @OneToMany(mappedBy = "member")
    private final List<Message> messages = new ArrayList<>();


    public static Member create(MemberSignupReqDto memberRequestDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(memberRequestDto.getEmail())
                .nickname(memberRequestDto.getNickname())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .authority(Authority.ROLE_USER)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    @Builder
    public Member(Long id, String email,String nickname, String password, Authority authority, int grade, MemberStatus status) {

        Assert.notNull(email, "???????????? ???????????????.");
        Assert.notNull(password, "??????????????? ???????????????.");

        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.authority = authority;
        this.grade = grade;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return grade == member.grade && Objects.equals(id, member.id) && Objects.equals(email, member.email) && Objects.equals(nickname, member.nickname) && Objects.equals(password, member.password) && authority == member.authority && status == member.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, nickname, password, authority, grade, status, messages);
    }

    public void changeStatus(MemberStatus status){
        this.status = status;
    }

    public Member changeNickname(MemberPatchReqDto memberPatchReqDto) {
        this.nickname = memberPatchReqDto.getNickname();
        return this;
    }
}

