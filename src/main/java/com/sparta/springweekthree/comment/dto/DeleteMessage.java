package com.sparta.springweekthree.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class DeleteMessage {
    private String msg;
    private HttpStatus httpStatus;

    public DeleteMessage(String msg, HttpStatus httpStatus) {
        this.msg = msg;
        this.httpStatus = httpStatus;
    }
}
