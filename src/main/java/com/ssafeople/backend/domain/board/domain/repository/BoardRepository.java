package com.ssafeople.backend.domain.board.domain.repository;

import com.ssafeople.backend.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Short> {
}
