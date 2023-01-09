package com.sparta.springweekthree.member.entity;

import com.sparta.springweekthree.member.dto.SignUpRequestDto;
import com.sparta.springweekthree.util.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

import static com.sparta.springweekthree.member.entity.MemberRole.USER;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
@Validated
public class Member extends TimeStamped {

    @Id @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public Member encryptPassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
        return this;
    }

    public Member(SignUpRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.role = USER;
    }
}
