package com.sparta.springweekthree.comment.controller;

import com.sparta.springweekthree.bulletinboard.dto.LikeResponseMessage;
import com.sparta.springweekthree.comment.dto.CommentForm;
import com.sparta.springweekthree.comment.dto.DeleteMessage;
import com.sparta.springweekthree.comment.service.CommentLikeService;
import com.sparta.springweekthree.comment.service.CommentService;
import com.sparta.springweekthree.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    //댓글 작성
    @PostMapping("/bulletin-boards/{board-id}/comments")
    public CommentForm write(@PathVariable(name = "board-id") Long boardId, @RequestBody CommentForm commentForm,
                             @AuthenticationPrincipal UserDetailsImpl memberDetails) {

        return commentService.create(boardId, commentForm, memberDetails.getMember());
    }

    //댓글 수정
    @PatchMapping("/comments/{comment-id}")
    public ResponseEntity<Object> update(@PathVariable(name = "comment-id") Long commentId,
                                         @RequestBody CommentForm commentForm,
                                         @AuthenticationPrincipal UserDetailsImpl memberDetails) throws IllegalAccessException {

        CommentForm comment = commentService.update(commentId, commentForm, memberDetails.getMember());
        return new ResponseEntity<>(comment, OK);
    }

    //댓글 삭제
    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity<Object> softDelete(@PathVariable(name = "comment-id") Long commentId,
                                             @AuthenticationPrincipal UserDetailsImpl memberDetails) throws IllegalAccessException {

        DeleteMessage deleteMessage = commentService.softDelete(commentId, memberDetails.getMember());
        return new ResponseEntity<>(deleteMessage, deleteMessage.getHttpStatus());
    }

    // 댓글 좋아요
    @PostMapping("/comments/{comment-id}/likes")
    public ResponseEntity<LikeResponseMessage> doLikeOfBoard(@PathVariable(name = "comment-id") Long commentId,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws IllegalAccessException {

        boolean isLike = commentLikeService.commentLikes(commentId, userDetails.getMember());

        if (isLike){
            LikeResponseMessage responseDto = new LikeResponseMessage("좋아요", OK);
            return new ResponseEntity<>(responseDto, responseDto.getStatus());
        }

        LikeResponseMessage responseDto = new LikeResponseMessage("좋아요 취소", OK);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }
}
