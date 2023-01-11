package com.sparta.springweekthree.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class CommentResponseMessage {
    private String msg;
    private HttpStatus httpStatus;
    private Object data;

    public CommentResponseMessage(String msg, HttpStatus httpStatus) {
        this.msg = msg;
        this.httpStatus = httpStatus;
    }

    public CommentResponseMessage(String msg,HttpStatus httpStatus, Object data) {
        this.httpStatus = httpStatus;
        this.msg = msg;
        this.data = data;
    }
}
