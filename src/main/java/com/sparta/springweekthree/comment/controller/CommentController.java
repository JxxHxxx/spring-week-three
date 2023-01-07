package com.sparta.springweekthree.comment.controller;

import com.sparta.springweekthree.comment.dto.CommentForm;
import com.sparta.springweekthree.comment.dto.DeleteMessage;
import com.sparta.springweekthree.comment.service.CommentService;
import com.sparta.springweekthree.exception.dto.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/bulletin-boards/{board-id}/comments")
    public CommentForm write(@PathVariable(name = "board-id") Long boardId, @RequestBody CommentForm commentForm, HttpServletRequest request) {
        return commentService.write(boardId, commentForm, request);
    }

    //댓글 수정
    @PatchMapping("/bulletin-boards/{board-id}/comments/{comment-id}")
    public ResponseEntity<Object> update(@PathVariable(name = "comment-id") Long commentId, @RequestBody CommentForm commentForm, HttpServletRequest request) {
        CommentForm comment = null;
        try {
            comment = commentService.update(commentId, commentForm, request);
        }
        catch (IllegalAccessException e) {
            return new ResponseEntity<>(new ExceptionMessage("작성자만 삭제/수정할 수 있습니다.", BAD_REQUEST), BAD_REQUEST);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(new ExceptionMessage("토큰이 유효하지 않습니다.", BAD_REQUEST), BAD_REQUEST);
        }

        return new ResponseEntity<>(comment, OK);
    }

    //댓글 삭제
    @DeleteMapping("/bulletin-boards/{board-id}/comments/{comment-id}")
    public ResponseEntity<Object> softDelete(@PathVariable(name = "comment-id") Long commentId, HttpServletRequest request) {
        DeleteMessage deleteMessage = null;
        try {
            deleteMessage = commentService.softDelete(commentId, request);
        }
        catch (IllegalAccessException e) {
            return new ResponseEntity<>(new ExceptionMessage("작성자만 삭제/수정할 수 있습니다.", BAD_REQUEST), BAD_REQUEST);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(new ExceptionMessage("토큰이 유효하지 않습니다.", BAD_REQUEST), BAD_REQUEST);
        }
        return new ResponseEntity<>(deleteMessage, deleteMessage.getHttpStatus());
    }
}
