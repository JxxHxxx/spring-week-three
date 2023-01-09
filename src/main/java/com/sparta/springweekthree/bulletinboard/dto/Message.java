package com.sparta.springweekthree.bulletinboard.dto;

import lombok.Getter;

@Getter
public class Message {

    private final String msg;
    private final Object data;

    public Message(String msg, Object data) {
        this.msg = msg;
        this.data = data;
    }
}
