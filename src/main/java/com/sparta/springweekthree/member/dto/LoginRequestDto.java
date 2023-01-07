package com.sparta.springweekthree.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {
    private String username;
    private String password;
}
