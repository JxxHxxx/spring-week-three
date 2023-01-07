package com.sparta.springweekthree.member.entity;

import com.sparta.springweekthree.Timestamped;
import com.sparta.springweekthree.member.dto.SignUpRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

import static com.sparta.springweekthree.member.entity.MemberGrade.USER;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
@Validated
public class Member extends Timestamped {

    @Id @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

    public Member(SignUpRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.grade = USER;
    }
}
