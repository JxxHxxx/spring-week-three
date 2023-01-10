package com.sparta.springweekthree.comment.entity;

import com.sparta.springweekthree.bulletinboard.entity.BulletinBoard;
import com.sparta.springweekthree.util.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean liked;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    public CommentLikes(Comment comment) {
        this.liked = true;
        this.comment = comment;
        this.comment.like();
    }

    public boolean press() {
        if (this.liked) {
            this.liked = false;
            this.comment.disLike();
            return this.liked;
        }

        this.liked = true;
        this.comment.like();
        return this.liked;
    }
}
