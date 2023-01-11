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

import static com.sparta.springweekthree.exception.message.IntegratedExceptionMessage.NOT_EXISTED_COMMENT;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public boolean commentLikes(Long commentId, Member member) {
        Optional<CommentLikes> optionalLikes = commentLikeRepository.findByComment_IdAndCreateBy(commentId, member.getId());
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_COMMENT.getMessage()));

        comment.isDeletedThenThrow();

        if (isFirstLikes(optionalLikes, comment)) {
            return true;
        }

        CommentLikes likes = optionalLikes.get();
        return likes.press();
    }

    private boolean isFirstLikes(Optional<CommentLikes> optionalLikes, Comment comment) {
        if (optionalLikes.isEmpty()) {
            commentLikeRepository.save(new CommentLikes(comment));
            return true;
        }
        return false;
    }
}
