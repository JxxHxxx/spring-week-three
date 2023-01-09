package com.sparta.springweekthree.bulletinboard.repository;

import com.sparta.springweekthree.bulletinboard.entity.BulletinBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BulletinBoardRepository extends JpaRepository<BulletinBoard, Long> {

    List<BulletinBoard> findAllByOrderByCreateAtDesc();
    Optional<BulletinBoard> findByIdAndMemberId(Long id, Long userId);
}
