package com.sparta.springweekthree.comment.service;

import com.sparta.springweekthree.bulletinboard.entity.BulletinBoard;
import com.sparta.springweekthree.bulletinboard.repository.BulletinBoardRepository;
import com.sparta.springweekthree.comment.dto.CommentForm;
import com.sparta.springweekthree.comment.dto.DeleteMessage;
import com.sparta.springweekthree.comment.entity.Comment;
import com.sparta.springweekthree.comment.repository.CommentRepository;
import com.sparta.springweekthree.member.entity.Member;
import com.sparta.springweekthree.member.entity.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BulletinBoardRepository bulletinBoardRepository;

    private final static String ILLEGAL_ACCESS_MESSAGE = "작성자 혹은 관리자만 삭제/수정할 수 있습니다.";

    public CommentForm write(Long boardId, CommentForm commentForm, Member member) throws IllegalAccessException {
        BulletinBoard bulletinBoard = bulletinBoardRepository.findById(boardId).orElseThrow(); // 게시글 유무 확인

        if (bulletinBoard.getIsDeleted()) {
            throw new IllegalAccessException("존재하지 않은 게시글입니다.");
        }

        Comment comment = new Comment(commentForm, bulletinBoard, member.getUsername());
        commentRepository.save(comment);

        return commentForm;
    }

    @Transactional
    public CommentForm update(Long commentId, CommentForm commentForm, Member member) throws IllegalAccessException {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (comment.getCreateBy().equals(member.getId()) || member.getRole().equals(MemberRole.ADMIN)) {
            Comment updateComment = comment.update(commentForm.getBody());
            return new CommentForm(updateComment);
        }

        throw new IllegalAccessException(ILLEGAL_ACCESS_MESSAGE);
    }

    @Transactional
    public DeleteMessage softDelete(Long commentId, Member member) throws IllegalAccessException {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (comment.getCreateBy().equals(member.getId()) || member.getRole().equals(MemberRole.ADMIN)) {
            comment.softDelete();
            return new DeleteMessage("삭제 성공", OK);
        }

        throw  new IllegalAccessException(ILLEGAL_ACCESS_MESSAGE);
    }

    public List<Comment> read(Long id) {
        List<Comment> comments = commentRepository.findByBulletinBoard_Id(id);
        return comments.stream().filter(comment -> comment.getIsDeleted() != true).collect(Collectors.toList());
    }
}
