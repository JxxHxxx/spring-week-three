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

import static org.springframework.http.HttpStatus.OK;


@Service
@RequiredArgsConstructor
public class BulletinBoardService {

    private final BulletinBoardRepository bulletinBoardRepository;
    private final CommentService commentService;

    private final static String ILLEGAL_ACCESS_MESSAGE = "작성자 혹은 관리자만 삭제/수정할 수 있습니다.";

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        BulletinBoardResponseDto boardResponseDto = new BulletinBoardResponseDto(board);
        
        boardResponseDto.isDeleted();
        
        return boardResponseDto;
    }

    @Transactional
    public BoardResponseMessage softDelete(Long id, Member member) throws IllegalAccessException {
        BulletinBoard board = bulletinBoardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (hasAuthority(member, board)) {
            board.softDelete(true);
            return new BoardResponseMessage(OK, "삭제 완료");
        }

        throw new IllegalAccessException(ILLEGAL_ACCESS_MESSAGE);
    }

    @Transactional
    public BoardResponseMessage update(Long id, BulletinBoardForm boardForm, Member member) throws IllegalAccessException {
        BulletinBoard board = bulletinBoardRepository.findById(id).orElseThrow();

        if (hasAuthority(member, board)) {
            board.update(boardForm);
            return new BoardResponseMessage(OK, "수정 완료", new BulletinBoardResponseDto(board));
        }
        throw new IllegalAccessException(ILLEGAL_ACCESS_MESSAGE);
    }

    private boolean isExisted(BulletinBoard board) {
        return board.isDeleted() == false;
    }

    private boolean hasAuthority(Member member, BulletinBoard board) {
        return member.getId().equals(board.getCreateBy()) || member.getRole().equals(MemberRole.ADMIN);
    }
}
