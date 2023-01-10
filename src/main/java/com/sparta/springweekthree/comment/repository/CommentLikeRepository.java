package com.sparta.springweekthree.comment.repository;

import com.sparta.springweekthree.comment.entity.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLikes, Long> {
    Optional<CommentLikes> findByComment_IdAndCreateBy(Long commentId, Long createBy);
}
