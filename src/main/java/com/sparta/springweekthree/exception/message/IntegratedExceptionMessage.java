package com.sparta.springweekthree.exception.message;

import lombok.Getter;

@Getter
public enum IntegratedExceptionMessage {

    NOT_EXISTED_BULLETIN_BOARD("게시글이 존재하지 않습니다."),
    DELETED_BULLETIN_BOARD("삭제된 게시글입니다."),
    NOT_EXISTED_COMMENT("댓글이 존재하지 않습니다."),
    DELETED_COMMENT("삭제된 댓글입니다."),
    ILLEGAL_ACCESS_UPDATE_OR_DELETE("작성자 혹은 관리자만 수정/삭제 할 수 있습니다.");

    IntegratedExceptionMessage(String message) {
        this.message = message;
    }

    private final String message;


}
