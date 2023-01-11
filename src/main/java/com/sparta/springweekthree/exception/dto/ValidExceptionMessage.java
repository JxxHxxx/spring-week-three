package com.sparta.springweekthree.exception.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@NoArgsConstructor
public class ValidExceptionMessage {
    private List<String> messages;
    private Integer statusCode;

    public ValidExceptionMessage(List<String> messages, HttpStatus status) {
        this.messages = messages;
        this.statusCode = status.value();
    }
}
