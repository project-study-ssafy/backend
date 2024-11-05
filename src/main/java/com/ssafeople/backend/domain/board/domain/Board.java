package com.ssafeople.backend.domain.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Short id;

    @Column(nullable = false, unique = true)
    private String boardName;

    private String description;

    @Column(name = "created_at")
    @CreationTimestamp
    @Immutable
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void update(String newBoardName, String newDescription) {
        this.boardName = newBoardName;
        this.description = newDescription;
    }
}
