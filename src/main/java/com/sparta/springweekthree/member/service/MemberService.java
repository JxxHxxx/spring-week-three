package com.sparta.springweekthree.member.service;

import com.sparta.springweekthree.jwt.JwtUtil;
import com.sparta.springweekthree.member.dto.LoginRequestDto;
import com.sparta.springweekthree.member.dto.SignUpRequestDto;
import com.sparta.springweekthree.member.entity.Member;
import com.sparta.springweekthree.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final String SIGNUP_SUCCESS = "회원가입 성공";

    public String signUp(SignUpRequestDto requestDto) {
        if (memberRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 유저이름입니다.");
        }

        Member member = new Member(requestDto);
        member.encryptPassword(passwordEncoder);
        memberRepository.save(member);

        return SIGNUP_SUCCESS;
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호 확인
        if(!passwordEncoder.matches(password, member.getPassword())){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getUsername()));
    }
}
