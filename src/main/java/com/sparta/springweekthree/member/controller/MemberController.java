package com.sparta.springweekthree.member.controller;

import com.sparta.springweekthree.member.dto.AuthMessage;
import com.sparta.springweekthree.member.dto.LoginRequestDto;
import com.sparta.springweekthree.member.dto.SignUpRequestDto;
import com.sparta.springweekthree.member.service.MemberService;
import com.sparta.springweekthree.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/auth/signup")
    public ResponseEntity<AuthMessage> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        String message;
        try {
            message = memberService.signUp(signUpRequestDto);
        }
        catch (IllegalArgumentException e) {
            AuthMessage authMessage = new AuthMessage("중복된 username 입니다.", BAD_REQUEST.value());
            return new ResponseEntity<>(authMessage, BAD_REQUEST);
        }
        AuthMessage authMessage = new AuthMessage(message, OK.value());

        return new ResponseEntity<>(authMessage, OK);
    }
    @PostMapping("auth/login")
    public ResponseEntity<AuthMessage> login(@RequestBody LoginRequestDto loginDto, HttpServletResponse response) {
        try {
            memberService.login(loginDto, response);
        }
        catch (IllegalArgumentException e) {
            AuthMessage authMessage = new AuthMessage("회원을 찾을 수 없습니다.", BAD_REQUEST.value());
            return new ResponseEntity<>(authMessage, BAD_REQUEST);
        }

        AuthMessage authMessage = new AuthMessage("로그인 성공", OK.value());
        return new ResponseEntity<>(authMessage, OK);
    }

    @GetMapping("auth/admin")
    public ResponseEntity<Object> giveAdminRole(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long id = userDetails.getMember().getId();
        memberService.giveAdminRole(id);
        AuthMessage authMessage = new AuthMessage("관리자로 등록되었습니다.", OK.value());

        return new ResponseEntity<>(authMessage, OK);
    }
}
