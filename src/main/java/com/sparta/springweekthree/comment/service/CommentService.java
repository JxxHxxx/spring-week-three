package com.sparta.springweekthree.comment.service;

import com.sparta.springweekthree.bulletinboard.entity.BulletinBoard;
import com.sparta.springweekthree.bulletinboard.repository.BulletinBoardRepository;
import com.sparta.springweekthree.comment.dto.CommentForm;
import com.sparta.springweekthree.comment.dto.DeleteMessage;
import com.sparta.springweekthree.comment.entity.Comment;
import com.sparta.springweekthree.comment.repository.CommentRepository;
import com.sparta.springweekthree.jwt.JwtUtil;
import com.sparta.springweekthree.member.entity.Member;
import com.sparta.springweekthree.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.springweekthree.member.entity.MemberGrade.*;
import static org.springframework.http.HttpStatus.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BulletinBoardRepository bulletinBoardRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public CommentForm write(Long boardId, CommentForm commentForm, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (!jwtUtil.validateToken(token)) { // 유효한 토큰 검사
            return null;
        }
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        BulletinBoard bulletinBoard = bulletinBoardRepository.findById(boardId).orElseThrow(); // 게시글 유무 확인

        Comment comment = new Comment(commentForm, bulletinBoard, claims.getSubject());
        commentRepository.save(comment);

        return commentForm;
    }

    @Transactional
    public CommentForm update(Long commentId, CommentForm commentForm, HttpServletRequest request) throws IllegalAccessException {
        String token = jwtUtil.resolveToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Member member = memberRepository.findByUsername(claims.getSubject()).orElseThrow();
        if (!jwtUtil.validateToken(token) || member.getGrade().equals(ADMIN)) { // 유효한 토큰 검사
            return null;
        }


        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (!comment.getUsername().equals(member.getUsername())) {
            throw new IllegalAccessException("작성자만 삭제/수정할 수 있습니다.");
        }

        Comment updateComment = comment.update(commentForm.getBody());

        return new CommentForm(updateComment);
    }

    @Transactional
    public DeleteMessage softDelete(Long commentId, HttpServletRequest request) throws IllegalAccessException {
        String token = jwtUtil.resolveToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Member member = memberRepository.findByUsername(claims.getSubject()).orElseThrow();
        if (!jwtUtil.validateToken(token) || member.getGrade().equals(ADMIN)) { // 유효한 토큰 검사
            return new DeleteMessage("토큰이 유효하지 않습니다.", BAD_REQUEST);
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (!comment.getUsername().equals(member.getUsername())) {
            throw new IllegalAccessException("작성자만 삭제/수정할 수 있습니다.");
        }

        comment.setSoftDelete();

        return new DeleteMessage("삭제 성공", OK);
    }

    public List<Comment> read(Long id) {
        List<Comment> comments = commentRepository.findByBulletinBoard_Id(id);
        comments.forEach(comment -> log.info("comment : body = {}, isDeleted = {}", comment.getBody(), comment.getIsDeleted()));
        return comments.stream().filter(comment -> comment.getIsDeleted() != true).collect(Collectors.toList());
    }
}
