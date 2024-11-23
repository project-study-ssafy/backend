package com.ssafeople.backend.domain.post.domain;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.comment.domain.Comment;
import com.ssafeople.backend.domain.post.domain.vo.PostInfoVO;
import com.ssafeople.backend.domain.user.domain.User;
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
@Table(name = "posts")
@NoArgsConstructor
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    @CreationTimestamp
    @Immutable
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    public Post(String title, String content, User user, Board board) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.board = board;

        user.getPosts().add(this);
    }

    public PostInfoVO getPostInfo() {
        return PostInfoVO.builder()
                .id(id)
                .userId(user.getId())
                .boardId(board.getId())
                .title(title)
                .content(content)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .comments(comments)
                .build();
    }

}
