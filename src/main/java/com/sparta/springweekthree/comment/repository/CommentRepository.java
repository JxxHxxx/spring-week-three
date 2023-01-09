package com.sparta.springweekthree.comment.repository;

import com.sparta.springweekthree.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBulletinBoard_Id(Long id);
    Optional<Comment> findByIdAndUsername(Long id, String username);
}
