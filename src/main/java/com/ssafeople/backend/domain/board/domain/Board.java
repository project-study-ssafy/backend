package com.ssafeople.backend.domain.board.domain;

import com.ssafeople.backend.domain.post.domain.Post;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "boards")
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

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Post> posts = new ArrayList<>();

    public Board(String boardName, String description) {
        this.boardName = boardName;
        this.description = description;
    }
}
