package com.sparta.springweekthree.bulletinboard.dto;

import lombok.Getter;

@Getter
public class Message {

    private Boolean success;
    private Object data;

    public Message(Boolean success, Object data) {
        this.success = success;
        this.data = data;
    }
}
