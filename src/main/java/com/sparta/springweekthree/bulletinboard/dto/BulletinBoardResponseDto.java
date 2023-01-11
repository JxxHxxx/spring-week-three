package com.sparta.springweekthree.bulletinboard.dto;

import com.sparta.springweekthree.bulletinboard.entity.BulletinBoard;
import com.sparta.springweekthree.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BulletinBoardResponseDto {

    private Long id;
    private String title;
    private String body;
    private String username;
    private List<Comment> comment;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private Boolean isDeleted;
    private Integer totalLike;

    public BulletinBoardResponseDto(BulletinBoard board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.readUsername();
        this.comment = board.getComments();
        this.body = board.getBody();
        this.totalLike = board.getTotalLike();
        this.createAt = board.getCreateAt();
        this.modifiedAt = board.getModifiedAt();
        this.isDeleted = board.getIsDeleted();
    }

    public BulletinBoardResponseDto(BulletinBoard board, List<Comment> comments) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.readUsername();
        this.comment = comments;
        this.body = board.getBody();
        this.totalLike = board.getTotalLike();
        this.createAt = board.getCreateAt();
        this.modifiedAt = board.getModifiedAt();
        this.isDeleted = board.getIsDeleted();
    }

    public void isDeleted() {
        if (this.isDeleted) {
            throw new IllegalArgumentException("삭제된 게시글입니다.");
        }
    }
}
