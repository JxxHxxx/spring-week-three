package com.sparta.springweekthree.bulletinboard.controller;

import com.sparta.springweekthree.bulletinboard.service.BulletinBoardService;
import com.sparta.springweekthree.bulletinboard.dto.*;
import com.sparta.springweekthree.comment.entity.Comment;
import com.sparta.springweekthree.comment.service.CommentService;
import com.sparta.springweekthree.exception.dto.ExceptionMessage;
import com.sparta.springweekthree.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.sparta.springweekthree.security.UserDetailsImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
@Slf4j
@RestController
@RequiredArgsConstructor
public class BulletinBoardController {

    private final BulletinBoardService bulletinBoardService;
    private final CommentService commentService;
    private final LikeService likeService;

    // 게시글 작성
    @PostMapping("/bulletin-boards")
    public ResponseEntity<Object> write(@RequestBody BulletinBoardForm boardForm, @AuthenticationPrincipal UserDetailsImpl memberDetails) {
        return new ResponseEntity<>(bulletinBoardService.create(boardForm, memberDetails.getMember()), OK);
    }

    // 전체 게시글 조회
    @GetMapping("/bulletin-boards")
    public List<BulletinBoardResponseDto> readAll() {
        return bulletinBoardService.readAll();
    }
    // 선택 게시글 조회
    @GetMapping("/bulletin-boards/{id}")
    public BulletinBoardResponseDto readOne(@PathVariable Long id) {
        List<Comment> comments = commentService.read(id);
        BulletinBoardResponseDto board = bulletinBoardService.readOne(id);
        Integer totalLikes = likeService.calculateLikes(id);

        return new BulletinBoardResponseDto(board, comments, totalLikes);
    }
    // 선택 게시글 수정
    @PatchMapping("/bulletin-boards/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody BulletinBoardForm boardForm, @AuthenticationPrincipal UserDetailsImpl memberDetails) {
        Message message = null;
        try {
            message = bulletinBoardService.update(id, boardForm, memberDetails.getMember());
        } catch (IllegalAccessException e) {
            return new ResponseEntity<>(new ExceptionMessage(e.getMessage(), BAD_REQUEST), BAD_REQUEST);
        }

        return new ResponseEntity<>(message, OK);
    }

    // 선택 게시글 삭제
    @DeleteMapping("/bulletin-boards/{id}")
    public ResponseEntity<Object> remove(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl memberDetails) {
        ResultDto resultDto = null;
        try {
            resultDto = bulletinBoardService.softDelete(id, memberDetails.getMember());
        } catch (IllegalAccessException e) {
            return new ResponseEntity<>(new ExceptionMessage(e.getMessage(), BAD_REQUEST), BAD_REQUEST);
        }

        return new ResponseEntity<>(resultDto, OK);
    }
}
