package com.sparta.springweekthree.comment.service;

import com.sparta.springweekthree.bulletinboard.entity.BulletinBoard;
import com.sparta.springweekthree.bulletinboard.repository.BulletinBoardRepository;
import com.sparta.springweekthree.comment.dto.CommentForm;
import com.sparta.springweekthree.comment.dto.DeleteMessage;
import com.sparta.springweekthree.comment.entity.Comment;
import com.sparta.springweekthree.comment.repository.CommentRepository;
import com.sparta.springweekthree.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BulletinBoardRepository bulletinBoardRepository;

    public CommentForm write(Long boardId, CommentForm commentForm, Member member) {
        BulletinBoard bulletinBoard = bulletinBoardRepository.findById(boardId).orElseThrow(); // 게시글 유무 확인

        Comment comment = new Comment(commentForm, bulletinBoard, member.getUsername());
        commentRepository.save(comment);

        return commentForm;
    }

    @Transactional
    public CommentForm update(Long commentId, CommentForm commentForm, Member member) throws IllegalAccessException {
        Comment comment = commentRepository.findByIdAndUsername(commentId, member.getUsername())
                .orElseThrow(() -> new IllegalAccessException("작성자만 삭제/수정할 수 있습니다."));

        Comment updateComment = comment.update(commentForm.getBody());
        return new CommentForm(updateComment);
    }

    @Transactional
    public DeleteMessage softDelete(Long commentId, Member member) throws IllegalAccessException {
        Comment comment = commentRepository.findByIdAndUsername(commentId, member.getUsername())
                .orElseThrow(() -> new IllegalAccessException("작성자만 삭제/수정할 수 있습니다."));

        comment.setSoftDelete();
        return new DeleteMessage("삭제 성공", OK);
    }

    public List<Comment> read(Long id) {
        List<Comment> comments = commentRepository.findByBulletinBoard_Id(id);
        comments.forEach(comment -> log.info("comment : body = {}, isDeleted = {}", comment.getBody(), comment.getIsDeleted()));
        return comments.stream().filter(comment -> comment.getIsDeleted() != true).collect(Collectors.toList());
    }
}
