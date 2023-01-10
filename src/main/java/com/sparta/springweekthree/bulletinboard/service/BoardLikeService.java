package com.sparta.springweekthree.bulletinboard.service;

import com.sparta.springweekthree.bulletinboard.entity.BulletinBoard;
import com.sparta.springweekthree.bulletinboard.repository.BulletinBoardRepository;
import com.sparta.springweekthree.bulletinboard.entity.BoardLikes;
import com.sparta.springweekthree.bulletinboard.repository.BoardLikeRepository;
import com.sparta.springweekthree.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final BulletinBoardRepository bulletinBoardRepository;

    @Transactional
    public boolean boardLikes(Long boardId, Member member) {
        Optional<BoardLikes> optionalLikes = boardLikeRepository.findByBulletinBoard_IdAndCreateBy(boardId, member.getId());

        if (isFirstLike(boardId, optionalLikes)) {
            return true;
        }

        BoardLikes likes = optionalLikes.get();
        return likes.press();
    }

    private boolean isFirstLike(Long boardId, Optional<BoardLikes> optionalLikes) {
        if (optionalLikes.isEmpty()) {
            BulletinBoard bulletinBoard = bulletinBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

            boardLikeRepository.save(new BoardLikes(bulletinBoard));
            return true;
        }
        return false;
    }
}
