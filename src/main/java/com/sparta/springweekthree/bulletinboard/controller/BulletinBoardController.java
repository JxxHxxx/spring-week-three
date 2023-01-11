package com.sparta.springweekthree.bulletinboard.controller;

import com.sparta.springweekthree.bulletinboard.dto.BoardResponseMessage;
import com.sparta.springweekthree.bulletinboard.dto.BulletinBoardForm;
import com.sparta.springweekthree.bulletinboard.dto.BulletinBoardResponseDto;
import com.sparta.springweekthree.bulletinboard.dto.LikeResponseMessage;
import com.sparta.springweekthree.bulletinboard.service.BoardLikeService;
import com.sparta.springweekthree.bulletinboard.service.BulletinBoardService;
import com.sparta.springweekthree.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BulletinBoardController {

    private final BulletinBoardService bulletinBoardService;
    private final BoardLikeService boardLikeService;

    // 게시글 작성
    @PostMapping("/bulletin-boards")
    public ResponseEntity<BulletinBoardResponseDto> write(@RequestBody BulletinBoardForm boardForm,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return new ResponseEntity<>(bulletinBoardService.create(boardForm, userDetails.getMember()), OK);
    }

    // 전체 게시글 조회
    @GetMapping("/bulletin-boards")
    public List<BulletinBoardResponseDto> readAll() {

        return bulletinBoardService.readAll();
    }

    // 선택 게시글 조회
    @GetMapping("/bulletin-boards/{board-id}")
    public BulletinBoardResponseDto readOne(@PathVariable(name = "board-id") Long id) {

        return bulletinBoardService.readOne(id);
    }

    // 선택 게시글 수정
    @PatchMapping("/bulletin-boards/{board-id}")
    public ResponseEntity<BoardResponseMessage> update(@PathVariable(name = "board-id") Long id, @RequestBody BulletinBoardForm boardForm,
                                         @AuthenticationPrincipal UserDetailsImpl memberDetails) throws IllegalAccessException {

        BoardResponseMessage message = bulletinBoardService.update(id, boardForm, memberDetails.getMember());
        return new ResponseEntity<>(message, message.getStatus());
    }

    // 선택 게시글 삭제
    @DeleteMapping("/bulletin-boards/{board-id}")
    public ResponseEntity<BoardResponseMessage> remove(@PathVariable(name = "board-id") Long id,
                                         @AuthenticationPrincipal UserDetailsImpl memberDetails) throws IllegalAccessException {

        BoardResponseMessage okMessage = bulletinBoardService.softDelete(id, memberDetails.getMember());
        return new ResponseEntity<>(okMessage, OK);
    }

    // 게시글 좋아요/좋아요 취소
    @PostMapping("/bulletin-boards/{board-id}/likes")
    public ResponseEntity<LikeResponseMessage> doLikeOfBoard(@PathVariable(name = "board-id") Long id,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 좋아요, 좋아요 취소를 구분하는게 좋을까요?
        boolean isLiked = boardLikeService.boardLikes(id, userDetails.getMember());
        LikeResponseMessage likeResponseMessage = null;

        if(isLiked){
            likeResponseMessage = new LikeResponseMessage("좋아요", OK);
            return new ResponseEntity<>(likeResponseMessage, likeResponseMessage.getStatus());
        }

        likeResponseMessage = new LikeResponseMessage("좋아요 취소", OK);
        return new ResponseEntity<>(likeResponseMessage, likeResponseMessage.getStatus());
    }
}