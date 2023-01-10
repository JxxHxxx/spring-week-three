package com.sparta.springweekthree.comment.service;

import com.sparta.springweekthree.comment.entity.Comment;
import com.sparta.springweekthree.comment.entity.CommentLikes;
import com.sparta.springweekthree.comment.repository.CommentLikeRepository;
import com.sparta.springweekthree.comment.repository.CommentRepository;
import com.sparta.springweekthree.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public boolean commentLikes(Long boardId, Member member) {
        Optional<CommentLikes> optionalLikes = commentLikeRepository.findByComment_IdAndCreateBy(boardId, member.getId());

        if (isFirstLike(boardId, optionalLikes)) {
            return true;
        }

        CommentLikes likes = optionalLikes.get();
        return likes.press();
    }

    private boolean isFirstLike(Long boardId, Optional<CommentLikes> optionalLikes) {
        if (optionalLikes.isEmpty()) {
            Comment comment = commentRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

            commentLikeRepository.save(new CommentLikes(comment));
            return true;
        }
        return false;
    }
}
