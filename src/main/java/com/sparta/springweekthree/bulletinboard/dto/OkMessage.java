package com.sparta.springweekthree.bulletinboard.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OkMessage {

    private final HttpStatus status;
    private final String msg;
    private final Object data;

    public OkMessage(HttpStatus status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
}
