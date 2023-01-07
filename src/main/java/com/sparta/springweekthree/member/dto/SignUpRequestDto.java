package com.sparta.springweekthree.member.dto;

import lombok.Builder;
import lombok.Getter;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
public class SignUpRequestDto {

    @Size(min = 4, max = 10)
    @Pattern(regexp = "[0-9a-z]+")
    private String username;

    @Size(min = 8, max = 15)
    @Pattern(regexp = "[0-9a-zA-Z!@#$%^&*(),.?\":{}|<>]+")
    private String password;

    public SignUpRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
