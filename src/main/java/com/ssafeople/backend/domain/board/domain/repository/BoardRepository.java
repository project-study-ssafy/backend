package com.ssafeople.backend.domain.board.domain.repository;

import com.ssafeople.backend.domain.board.domain.Board;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface BoardRepository extends Repository<Board, Long> {

    List<Board> findAll();

    Board findById(Long id);
}
