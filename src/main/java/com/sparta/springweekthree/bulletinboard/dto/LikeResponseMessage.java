package com.sparta.springweekthree.bulletinboard.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LikeResponseMessage {

    private String msg;
    private HttpStatus status;

    public LikeResponseMessage(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
    }
}