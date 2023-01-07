package com.sparta.springweekthree.exception.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ExceptionMessage {
    private String msg;
    private HttpStatus status;

    public ExceptionMessage(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
    }
}
