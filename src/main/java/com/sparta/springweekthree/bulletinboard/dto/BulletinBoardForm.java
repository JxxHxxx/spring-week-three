package com.sparta.springweekthree.bulletinboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BulletinBoardForm {
    private String title;
    private String body;

    public BulletinBoardForm(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
