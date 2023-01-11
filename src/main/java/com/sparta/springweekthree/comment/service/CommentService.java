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

import static com.sparta.springweekthree.exception.message.IntegratedExceptionMessage.ILLEGAL_ACCESS_UPDATE_OR_DELETE;
import static com.sparta.springweekthree.exception.message.IntegratedExceptionMessage.NOT_EXISTED_BULLETIN_BOARD;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BulletinBoardRepository bulletinBoardRepository;

    public CommentForm create(Long boardId, CommentForm commentForm, Member member) {
        BulletinBoard bulletinBoard = bulletinBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_BULLETIN_BOARD.getMessage()));

        bulletinBoard.isDeletedThenThrow();

        Comment comment = new Comment(commentForm, bulletinBoard, member.getUsername());
        commentRepository.save(comment);

        return commentForm;
    }

    @Transactional
    public CommentForm update(Long commentId, CommentForm commentForm, Member member) throws IllegalAccessException {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (hasAuthority(member, comment)) {
            Comment updateComment = comment.update(commentForm.getBody());
            return new CommentForm(updateComment);
        }

        throw new IllegalAccessException(ILLEGAL_ACCESS_UPDATE_OR_DELETE.getMessage());
    }

    @Transactional
    public DeleteMessage softDelete(Long commentId, Member member) throws IllegalAccessException {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (hasAuthority(member, comment)) {
            comment.softDelete();
            return new DeleteMessage("삭제 성공", OK);
        }

        throw new IllegalAccessException(ILLEGAL_ACCESS_UPDATE_OR_DELETE.getMessage());
    }

    public List<Comment> read(Long id) {
        List<Comment> comments = commentRepository.findByBulletinBoard_Id(id);

        return comments.stream().filter(comment -> isExisted(comment)).collect(Collectors.toList());
    }

    private static boolean isExisted(Comment comment) {
        return comment.getIsDeleted() == false;
    }

    private boolean hasAuthority(Member member, Comment comment) {
        return comment.getCreateBy().equals(member.getId()) || member.getRole().equals(MemberRole.ADMIN);
    }
}
