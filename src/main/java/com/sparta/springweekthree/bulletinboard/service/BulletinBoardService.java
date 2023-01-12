package com.sparta.springweekthree.bulletinboard.service;

import com.sparta.springweekthree.bulletinboard.dto.BoardResponseMessage;
import com.sparta.springweekthree.bulletinboard.dto.BulletinBoardForm;
import com.sparta.springweekthree.bulletinboard.dto.BulletinBoardResponseDto;
import com.sparta.springweekthree.bulletinboard.entity.BulletinBoard;
import com.sparta.springweekthree.bulletinboard.repository.BulletinBoardRepository;
import com.sparta.springweekthree.comment.service.CommentService;
import com.sparta.springweekthree.member.entity.Member;
import com.sparta.springweekthree.member.entity.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.springweekthree.exception.message.ServiceExceptionMessage.ILLEGAL_ACCESS_UPDATE_OR_DELETE;
import static com.sparta.springweekthree.exception.message.ServiceExceptionMessage.NOT_EXISTED_BULLETIN_BOARD;
import static org.springframework.http.HttpStatus.OK;


@Service
@RequiredArgsConstructor
public class BulletinBoardService {

    private final BulletinBoardRepository bulletinBoardRepository;
    private final CommentService commentService;

    public BulletinBoardResponseDto create(BulletinBoardForm boardForm, Member member) {
        BulletinBoard board = new BulletinBoard(boardForm, member);

        return new BulletinBoardResponseDto(bulletinBoardRepository.save(board));
    }

    public List<BulletinBoardResponseDto> readAll() {
        List<BulletinBoard> boards = bulletinBoardRepository.findAllByOrderByCreateAtDesc()
                .stream().filter(board -> isExisted(board)).collect(Collectors.toList());

        return boards.stream().map(bulletinBoard -> new BulletinBoardResponseDto(bulletinBoard, commentService.read(bulletinBoard.getId())))
                .collect(Collectors.toList());
    }

    public BulletinBoardResponseDto readOne(Long id) {
        BulletinBoard board = bulletinBoardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_BULLETIN_BOARD.getMessage()));

        board.isDeletedThenThrow();

        BulletinBoardResponseDto boardResponseDto = new BulletinBoardResponseDto(board);
        return boardResponseDto;
    }

    @Transactional
    public BoardResponseMessage softDelete(Long id, Member member) throws IllegalAccessException {
        BulletinBoard board = bulletinBoardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_BULLETIN_BOARD.getMessage()));

        board.isDeletedThenThrow();

        if (hasAuthority(member, board)) {
            board.softDelete(true);
            return new BoardResponseMessage(OK, "삭제 완료");
        }

        throw new IllegalAccessException(ILLEGAL_ACCESS_UPDATE_OR_DELETE.getMessage());
    }

    @Transactional
    public BoardResponseMessage update(Long id, BulletinBoardForm boardForm, Member member) throws IllegalAccessException {
        BulletinBoard board = bulletinBoardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_BULLETIN_BOARD.getMessage()));

        board.isDeletedThenThrow();

        if (hasAuthority(member, board)) {
            board.update(boardForm);
            return new BoardResponseMessage(OK, "수정 완료", new BulletinBoardResponseDto(board));
        }
        throw new IllegalAccessException(ILLEGAL_ACCESS_UPDATE_OR_DELETE.getMessage());
    }

    private boolean isExisted(BulletinBoard board) {
        return board.isDeleted() == false;
    }

    private boolean hasAuthority(Member member, BulletinBoard board) {
        return member.getId().equals(board.getCreateBy()) || member.getRole().equals(MemberRole.ADMIN);
    }
}
