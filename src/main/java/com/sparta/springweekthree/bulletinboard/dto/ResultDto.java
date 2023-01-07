package com.sparta.springweekthree.bulletinboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResultDto {
    private boolean success;

    public ResultDto(boolean success) {
        this.success = success;
    }
}
