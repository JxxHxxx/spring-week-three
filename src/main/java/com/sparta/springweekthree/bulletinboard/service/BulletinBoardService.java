package com.sparta.springweekthree.bulletinboard.service;
import com.sparta.springweekthree.bulletinboard.dto.*;
import com.sparta.springweekthree.bulletinboard.entity.BulletinBoard;
import com.sparta.springweekthree.bulletinboard.repository.BulletinBoardRepository;
import com.sparta.springweekthree.comment.service.CommentService;
import com.sparta.springweekthree.jwt.JwtUtil;
import com.sparta.springweekthree.member.entity.Member;
import com.sparta.springweekthree.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.springweekthree.member.entity.MemberGrade.*;

@Service
@RequiredArgsConstructor
public class BulletinBoardService {

    private final BulletinBoardRepository bulletinBoardRepository;
    private final MemberRepository memberRepository;
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    public BulletinBoardResponseDto create(BulletinBoardForm boardForm, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);

        if (token == null) {
            return null;
        }

        if (!jwtUtil.validateToken(token)) {
            return null;
        }

        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Member member = memberRepository.findByUsername(claims.getSubject()).orElseThrow();

        BulletinBoard board = new BulletinBoard(boardForm, member);
        BulletinBoard saveBoard = bulletinBoardRepository.save(board);

        return new BulletinBoardResponseDto(saveBoard);
    }

    public List<BulletinBoardResponseDto> readAll() {
        List<BulletinBoard> boards = bulletinBoardRepository.findAllByOrderByCreateAtDesc()
                .stream().filter(bulletinBoard -> bulletinBoard.getIsDeleted() == null).collect(Collectors.toList());

        return boards.stream().map(bulletinBoard -> new BulletinBoardResponseDto(bulletinBoard, commentService.read(bulletinBoard.getId()))).collect(Collectors.toList());

    }

    public BulletinBoardResponseDto readOne(Long id) {
        BulletinBoard board = bulletinBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (board.getIsDeleted() != null) {
            throw new IllegalArgumentException("삭제된 게시글입니다.");
        }

        return new BulletinBoardResponseDto(board);
    }

    @Transactional
    public ResultDto softDelete(Long id, HttpServletRequest request) throws IllegalAccessException {
        BulletinBoard board = bulletinBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        String token = jwtUtil.resolveToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Member member = memberRepository.findByUsername(claims.getSubject()).orElseThrow();

        if (token == null) {
            return null;
        }

        if (!jwtUtil.validateToken(token) || member.getGrade().equals(ADMIN)) {
            return null;
        }

        if (!board.getMember().getId().equals(member.getId())) {
            throw new IllegalAccessException("작성자만 삭제/수정할 수 있습니다.");
        }

        board.softDelete(true);

        return new ResultDto(true);
    }

    @Transactional
    public Message update(Long id, BulletinBoardForm boardForm, HttpServletRequest request) throws IllegalAccessException {
        BulletinBoard board = bulletinBoardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        String token = jwtUtil.resolveToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Member member = memberRepository.findByUsername(claims.getSubject()).orElseThrow();

        log.info("board Id = {} memberId = {}", id, member.getId());
        BulletinBoard board = bulletinBoardRepository.findByIdAndMemberId(id, member.getId())
                .orElseThrow(() -> new IllegalAccessException("작성자만 삭제/수정 할 수 있습니다."));

        board.update(boardForm);

        return new Message("수정 성공", new BulletinBoardResponseDto(board));
    }
}
