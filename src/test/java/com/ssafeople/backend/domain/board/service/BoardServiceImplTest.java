package com.ssafeople.backend.domain.board.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.domain.repository.BoardRepository;
import com.ssafeople.backend.domain.board.presentation.dto.response.BoardInfoResponse;
import com.ssafeople.backend.global.exception.board.BoardListEmptyException;
import com.ssafeople.backend.global.exception.board.BoardNotInRepositoryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BoardServiceImplTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("게시판 목록 반환 성공")
    void getAllBoards_success() {
        Board board1 = new Board("newBoardName1", "Description1");
        Board board2 = new Board("newBoardName2", "Description2");

        boardRepository.save(board1);
        boardRepository.save(board2);

        List<BoardInfoResponse> boards = boardService.getAllBoards();

        assertThat(boards.size()).isEqualTo(2);
        assertThat(boards).extracting(BoardInfoResponse::getBoardName).containsExactlyInAnyOrder("newBoardName1", "newBoardName2");
    }

    @Test
    @DisplayName("게시판 목록 조회가 실패하면 예외가 발생한다.")
    void getAllBoards_fail() {
        boardRepository.deleteAll();
        assertThrows(BoardListEmptyException.class, () -> boardService.getAllBoards());
    }

    @Test
    @DisplayName("특정 게시판 조회 성공")
    void getBoardById_success() {
        Board board1 = new Board("newBoardName1", "Description1");
        boardRepository.save(board1);

        Board board = boardService.getBoardById(board1.getId());

        assertThat(board).isEqualTo(board1);
    }

    @Test
    @DisplayName("게시판 없는거 조회")
    void getBoardById_fail() {
        Board board1 = new Board("newBoardName1", "Description1");
        boardRepository.save(board1);

        Short a = 999;
        assertThrows(BoardNotInRepositoryException.class, () -> boardService.getBoardById(a));
   }

}