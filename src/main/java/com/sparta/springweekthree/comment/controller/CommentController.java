package com.sparta.springweekthree.comment.controller;

import com.sparta.springweekthree.comment.dto.CommentForm;
import com.sparta.springweekthree.comment.dto.DeleteMessage;
import com.sparta.springweekthree.comment.service.CommentLikeService;
import com.sparta.springweekthree.comment.service.CommentService;
import com.sparta.springweekthree.exception.dto.ExceptionMessage;
import com.sparta.springweekthree.bulletinboard.dto.LikeResponseDto;
import com.sparta.springweekthree.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    //댓글 작성
    @PostMapping("/bulletin-boards/{board-id}/comments")
    public CommentForm write(@PathVariable(name = "board-id") Long boardId, @RequestBody CommentForm commentForm, @AuthenticationPrincipal UserDetailsImpl memberDetails) throws IllegalAccessException {
        return commentService.write(boardId, commentForm, memberDetails.getMember());
    }

    //댓글 수정
    @PatchMapping("/comments/{comment-id}")
    public ResponseEntity<Object> update(@PathVariable(name = "comment-id") Long commentId, @RequestBody CommentForm commentForm, @AuthenticationPrincipal UserDetailsImpl memberDetails) {
        CommentForm comment = null;
        try {
            comment = commentService.update(commentId, commentForm, memberDetails.getMember());
        }
        catch (IllegalAccessException e) {
            return new ResponseEntity<>(new ExceptionMessage(e.getMessage(), BAD_REQUEST), BAD_REQUEST);
        }

        return new ResponseEntity<>(comment, OK);
    }

    //댓글 삭제
    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity<Object> softDelete(@PathVariable(name = "comment-id") Long commentId, @AuthenticationPrincipal UserDetailsImpl memberDetails) {
        DeleteMessage deleteMessage = null;
        try {
            deleteMessage = commentService.softDelete(commentId, memberDetails.getMember());
        }
        catch (IllegalAccessException e) {
            return new ResponseEntity<>(new ExceptionMessage(e.getMessage(), BAD_REQUEST), BAD_REQUEST);
        }

        return new ResponseEntity<>(deleteMessage, deleteMessage.getHttpStatus());
    }

    // 댓글 좋아요
    @PostMapping("/comments/{comment-id}/likes")
    public ResponseEntity<LikeResponseDto> doLikeOfBoard(@PathVariable(name = "comment-id") Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IllegalAccessException {
        boolean isLike = commentLikeService.commentLikes(commentId, userDetails.getMember());

        if (isLike) {
            LikeResponseDto responseDto = new LikeResponseDto("좋아요", OK);
            return new ResponseEntity<>(responseDto, responseDto.getStatus());
        }

        LikeResponseDto responseDto = new LikeResponseDto("좋아요 취소", OK);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
