package com.sparta.springweekthree.bulletinboard.repository;

import com.sparta.springweekthree.bulletinboard.entity.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLikes, Long> {
    Optional<BoardLikes> findByBulletinBoard_IdAndCreateBy(Long BoardId, Long createdBy);
}
