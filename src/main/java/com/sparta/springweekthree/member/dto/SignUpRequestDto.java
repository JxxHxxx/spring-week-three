package com.sparta.springweekthree.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
@RequiredArgsConstructor
public class SignUpRequestDto {

    @Size(min = 4, max = 10, message = "닉네임은 4자에서 10자 사이여야 합니다.")
    @Pattern(regexp = "[0-9a-z]+", message = "닉네임은 소문자, 숫자만 가능합니다.")
    private String username;

    @Size(min = 8, max = 15, message = "비밀번호는 4자에서 10자 사이여야 합니다.")
    @Pattern(regexp = "[0-9a-zA-Z!@#$%^&*(),.?\":{}|<>]+", message = "비밀번호는 소문자, 대문자, 숫자, !@#$%^&*(),.?\":{}|<>만 가능합니다.")
    private String password;

    public SignUpRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
