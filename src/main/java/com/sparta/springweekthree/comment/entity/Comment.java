package com.sparta.springweekthree.comment.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.springweekthree.Timestamped;
import com.sparta.springweekthree.bulletinboard.entity.BulletinBoard;
import com.sparta.springweekthree.comment.dto.CommentForm;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.*;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id @Column(name = "COMMENT_ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String username;
    @Column(nullable = false)
    private String body;
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "BULLETIN_BOARD_ID")
    @JsonProperty(access = WRITE_ONLY)
    private BulletinBoard bulletinBoard;

    public Comment(CommentForm commentForm, BulletinBoard bulletinBoard, String username) {
        this.username = username;
        this.body = commentForm.getBody();
        this.bulletinBoard = bulletinBoard;
        this.isDeleted = false;
    }

    public Comment update(String body) {
        this.body = body;

        return this;
    }

    public void setSoftDelete() {
        this.isDeleted = true;
    }
}