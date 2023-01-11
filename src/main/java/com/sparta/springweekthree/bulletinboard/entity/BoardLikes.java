package com.sparta.springweekthree.bulletinboard.entity;

import com.sparta.springweekthree.util.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardLikes extends BaseEntity {

    @Id @Column(name = "BOARD_LIKES_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean isLiked;

    @ManyToOne
    @JoinColumn(name = "BULLETIN_BOARD_ID")
    private BulletinBoard bulletinBoard;

    public BoardLikes(BulletinBoard bulletinBoard) {
        this.isLiked = true;
        this.bulletinBoard = bulletinBoard;
        this.bulletinBoard.like();
    }

    public boolean press() {
        if (this.isLiked) {
            this.isLiked = false;
            this.bulletinBoard.disLike();
            return this.isLiked;
        }

        this.isLiked = true;
        this.bulletinBoard.like();
        return this.isLiked;
    }
}
