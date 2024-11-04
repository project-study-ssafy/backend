package com.ssafeople.backend.domain.board.domain;

import com.ssafeople.backend.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Board")
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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private final List<Board> boards = new ArrayList<>();

    public void update(String newBoardName, String newDescription) {
        this.boardName = newBoardName;
        this.description = newDescription;
    }
}
