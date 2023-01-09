package com.sparta.springweekthree.bulletinboard.entity;

import com.sparta.springweekthree.util.BaseEntity;
import com.sparta.springweekthree.bulletinboard.dto.BulletinBoardForm;
import com.sparta.springweekthree.comment.entity.Comment;
import com.sparta.springweekthree.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BulletinBoard extends BaseEntity {

    @Id @Column(name = "BULLETIN_BOARD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String body;
    private Boolean isDeleted;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "bulletinBoard", fetch = LAZY)
    private List<Comment> comments = new ArrayList<>();

    public BulletinBoard(BulletinBoardForm boardForm, Member member) {
        this.title = boardForm.getTitle();
        this.body = boardForm.getBody();
        this.member = member;
        this.isDeleted = null;
    }

    public void update(BulletinBoardForm boardForm) {
        this.title = boardForm.getTitle();
        this.body = boardForm.getBody();
    }
    public String readUsername() {
        return this.member.getUsername();
    }

    public void softDelete(Boolean bool) {
        this.isDeleted = bool;
    }
}
