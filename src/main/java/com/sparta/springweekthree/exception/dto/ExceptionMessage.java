package com.sparta.springweekthree.exception.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ExceptionMessage {
    private Integer statusCode;
    private String msg;

    public ExceptionMessage(String msg, HttpStatus statusCode) {
        this.msg = msg;
        this.statusCode = statusCode.value();
    }
}
