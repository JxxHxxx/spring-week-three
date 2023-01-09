package com.sparta.springweekthree.bulletinboard.controller;

import com.sparta.springweekthree.bulletinboard.service.BulletinBoardService;
import com.sparta.springweekthree.bulletinboard.dto.*;
import com.sparta.springweekthree.comment.entity.Comment;
import com.sparta.springweekthree.comment.service.CommentService;
import com.sparta.springweekthree.exception.dto.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.sparta.springweekthree.security.MemberDetailsImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
@Slf4j
@RestController
@RequiredArgsConstructor
public class BulletinBoardController {

    private final BulletinBoardService bulletinBoardService;
    private final CommentService commentService;

    // 게시글 작성
    @PostMapping("/bulletin-boards")
    public BulletinBoardResponseDto write(@RequestBody BulletinBoardForm boardForm, HttpServletRequest request) {
        return bulletinBoardService.create(boardForm, request);
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

        return new BulletinBoardResponseDto(board, comments);
    }

    // 선택 게시글 수정
    @PatchMapping("/bulletin-boards/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody BulletinBoardForm boardForm, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
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
    public ResponseEntity<Object> remove(@PathVariable Long id, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        ResultDto resultDto = null;
        try {
            resultDto = bulletinBoardService.softDelete(id, memberDetails.getMember());
        } catch (IllegalAccessException e) {
            return new ResponseEntity<>(new ExceptionMessage(e.getMessage(), BAD_REQUEST), BAD_REQUEST);
        }

        return new ResponseEntity<>(resultDto, OK);
    }
}
