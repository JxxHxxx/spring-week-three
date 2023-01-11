package com.sparta.springweekthree.comment.entity;

import com.sparta.springweekthree.util.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikes extends BaseEntity {

    @Id @Column(name = "COMMENT_LIKES_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean isLiked;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    public CommentLikes(Comment comment) {
        this.isLiked = true;
        this.comment = comment;
        this.comment.like();
    }

    public boolean press() {
        if (this.isLiked) {
            this.isLiked = false;
            this.comment.disLike();
            return this.isLiked;
        }

        this.isLiked = true;
        this.comment.like();
        return this.isLiked;
    }
}
