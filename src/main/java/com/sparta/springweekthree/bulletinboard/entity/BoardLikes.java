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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean liked;

    @ManyToOne
    @JoinColumn(name = "BULLETIN_BOARD_ID")
    private BulletinBoard bulletinBoard;

    public BoardLikes(BulletinBoard bulletinBoard) {
        this.liked = true;
        this.bulletinBoard = bulletinBoard;
        this.bulletinBoard.like();
    }

    public boolean press() {
        if (this.liked) {
            this.liked = false;
            this.bulletinBoard.disLike();
            return this.liked;
        }

        this.liked = true;
        this.bulletinBoard.like();
        return this.liked;
    }
}
