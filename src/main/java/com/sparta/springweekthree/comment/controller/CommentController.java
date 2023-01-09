package com.sparta.springweekthree.comment.controller;

import com.sparta.springweekthree.comment.dto.CommentForm;
import com.sparta.springweekthree.comment.dto.DeleteMessage;
import com.sparta.springweekthree.comment.service.CommentService;
import com.sparta.springweekthree.exception.dto.ExceptionMessage;
import com.sparta.springweekthree.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/bulletin-boards/{board-id}/comments")
    public CommentForm write(@PathVariable(name = "board-id") Long boardId, @RequestBody CommentForm commentForm, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return commentService.write(boardId, commentForm, memberDetails.getMember());
    }

    //댓글 수정
    @PatchMapping("/bulletin-boards/{board-id}/comments/{comment-id}")
    public ResponseEntity<Object> update(@PathVariable(name = "comment-id") Long commentId, @RequestBody CommentForm commentForm, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        CommentForm comment = null;
        try {
            comment = commentService.update(commentId, commentForm, memberDetails.getMember());
        }
        catch (IllegalAccessException e) {
            return new ResponseEntity<>(new ExceptionMessage("작성자만 삭제/수정할 수 있습니다.", BAD_REQUEST), BAD_REQUEST);
        }

        return new ResponseEntity<>(comment, OK);
    }

    //댓글 삭제
    @DeleteMapping("/bulletin-boards/{board-id}/comments/{comment-id}")
    public ResponseEntity<Object> softDelete(@PathVariable(name = "comment-id") Long commentId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        DeleteMessage deleteMessage = null;
        try {
            deleteMessage = commentService.softDelete(commentId, memberDetails.getMember());
        }
        catch (IllegalAccessException e) {
            return new ResponseEntity<>(new ExceptionMessage("작성자만 삭제/수정할 수 있습니다.", BAD_REQUEST), BAD_REQUEST);
        }

        return new ResponseEntity<>(deleteMessage, deleteMessage.getHttpStatus());
    }
}
