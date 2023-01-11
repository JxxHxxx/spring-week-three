package com.sparta.springweekthree.bulletinboard.service;

import com.sparta.springweekthree.bulletinboard.entity.BoardLikes;
import com.sparta.springweekthree.bulletinboard.entity.BulletinBoard;
import com.sparta.springweekthree.bulletinboard.repository.BoardLikeRepository;
import com.sparta.springweekthree.bulletinboard.repository.BulletinBoardRepository;
import com.sparta.springweekthree.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.springweekthree.exception.message.IntegratedExceptionMessage.NOT_EXISTED_BULLETIN_BOARD;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final BulletinBoardRepository bulletinBoardRepository;

    @Transactional
    public boolean boardLikes(Long boardId, Member member) {
        Optional<BoardLikes> optionalLikes = boardLikeRepository.findByBulletinBoard_IdAndCreateBy(boardId, member.getId());

        if (optionalLikes.isEmpty()) {
            BulletinBoard bulletinBoard = bulletinBoardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_BULLETIN_BOARD.getMessage()));

            bulletinBoard.isDeletedThenThrow();

            boardLikeRepository.save(new BoardLikes(bulletinBoard));
            return true;
        }

        BoardLikes likes = optionalLikes.get();
        return likes.press();
    }
}
