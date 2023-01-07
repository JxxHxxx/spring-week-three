//package com.sparta.springweektwo.bulletinboard.domain;
//
//import com.sparta.springweektwo.bulletinboard.dto.*;
//import com.sparta.springweektwo.bulletinboard.entity.BulletinBoard;
//import com.sparta.springweektwo.bulletinboard.repository.BulletinBoardRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//class BulletinBoardServiceTest {
//
//    @Autowired
//    BulletinBoardService bulletinBoardService;
//
//    @Autowired
//    BulletinBoardRepository bulletinBoardRepository;
//
//    @BeforeEach
//    void beforeEach() {
//        BulletinBoardForm bulletinBoardForm1 = new BulletinBoardForm("포도","나는 포도");
//        BulletinBoardForm bulletinBoardForm2 = new BulletinBoardForm("자몽","나는 자몽");
////        bulletinBoardService.create(bulletinBoardForm1);
////        bulletinBoardService.create(bulletinBoardForm2);
//    }
//    @AfterEach
//    void afterEach() {
//        bulletinBoardRepository.deleteAll();
//    }
//
//    @DisplayName("게시글 저장 - Id 고유값")
//    @Test
//    void hasUniqueId() {
//        List<BulletinBoardResponseDto> boardDtoList = bulletinBoardService.readAll();
//
//        assertThat(boardDtoList.get(0).getId()).isNotEqualTo(boardDtoList.get(1).getId());
//    }
//
//    @DisplayName("게시글 저장 - DB에 반영")
//    @Test
//    void saveOK() {
////        BulletinBoardForm bulletinBoardForm = new BulletinBoardForm("!234","감자","나는 감자");
////        BulletinBoardDto boardDto = bulletinBoardService.create(bulletinBoardForm);
//
////        assertThat(boardDto.getTitle()).isEqualTo("감자");
//    }
//
//    @DisplayName("게시글 수정 - 비밀번호 일치 시 - 수정 된 Entity 저장")
//    @Test
//    void UpdateSuccess() {
//        //given
//        BulletinBoard findBoard = bulletinBoardRepository.findByTitle("포도");
//        BulletinBoardForm updateForm = new BulletinBoardForm("123a","포도","나는 사실 샤인머스켓");
//
//        //when
//        Message message = bulletinBoardService.update(findBoard.getId(), updateForm);
//        BulletinBoard updateBoard = bulletinBoardRepository.findByTitle("포도");
//
//        //then
//        assertThat(message.getSuccess()).isTrue();
//        assertThat(updateBoard.getBody()).isEqualTo("나는 사실 샤인머스켓");
//    }
//
//    @DisplayName("게시글 수정 - 비밀번호 불일치 시 - 수정 전 Entity 저장")
//    @Test
//    void updateFail() {
//        //given
//        BulletinBoard findBoard = bulletinBoardRepository.findByTitle("자몽");
//
//        //when
//        BulletinBoardForm updateForm = new BulletinBoardForm("12123123","자몽","나는 사실 망고");
//        Message message = bulletinBoardService.update(findBoard.getId(), updateForm);
//
//        //then
//        Assertions.assertThat(message.getSuccess()).isFalse();
//        BulletinBoard updateBoard = bulletinBoardRepository.findByTitle("자몽");
//
//        Assertions.assertThat(updateBoard.getBody()).isEqualTo("나는 자몽");
//    }
//
//    @DisplayName("게시글 삭제 - 비밀번호 일치 시- 게시글 null")
//    @Test
//    void deleteSuccess() {
//        //given
//        BulletinBoard findBoard = bulletinBoardRepository.findByTitle("자몽");
//
//        //when
//        ResultDto result = bulletinBoardService.softDelete(findBoard.getId(), new PasswordDto("123a"));
//
//        //then
//        Assertions.assertThat(result.isSuccess()).isTrue();
//        Assertions.assertThatThrownBy(()-> bulletinBoardService.readOne(findBoard.getId())).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("게시글 삭제 - 비밀번호 불일치 시 - 게시글 유지")
//    @Test
//    void deleteFail() {
//        //given
//        BulletinBoard findBoard = bulletinBoardRepository.findByTitle("자몽");
//
//        //when
//        ResultDto result = bulletinBoardService.softDelete(findBoard.getId(), new PasswordDto("!2324"));
//
//        //then
//        Assertions.assertThat(result.isSuccess()).isFalse();
//        Assertions.assertThat(findBoard.getTitle()).isEqualTo("자몽");
//        Assertions.assertThat(findBoard.getBody()).isEqualTo("나는 자몽");
//        Assertions.assertThat(findBoard.getPassword()).isEqualTo("123a");
//    }
//}