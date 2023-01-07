package com.sparta.springweekthree.comment.dto;


import com.sparta.springweekthree.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentForm {
    private String body;

    public CommentForm(Comment comment) {
        this.body = comment.getBody();
    }
}


