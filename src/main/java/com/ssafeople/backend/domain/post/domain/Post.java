package com.ssafeople.backend.domain.post.domain;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.comment.domain.Comment;
import com.ssafeople.backend.domain.like.domain.Like;
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

    @Column(name = "view_count")
    private Short viewCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private final List<Like> likes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "post_image_urls", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "image_url")
    private final List<String> imageUrls = new ArrayList<>();

    public Post(String title, String content, User user, Board board) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.board = board;
        this.viewCount = (short) 0;

        user.getPosts().add(this);
    }

    public Post(String title, String content, User user, Board board, List<String> ImageUrls) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.board = board;

        this.imageUrls.addAll(ImageUrls);

        user.getPosts().add(this);
    }

    public void update(String title, String content, List<String> urls) {
        this.title = title;
        this.content = content;

        this.imageUrls.clear();
        this.imageUrls.addAll(urls);
        this.updatedAt = LocalDateTime.now();
    }

    public void view() {
        this.viewCount++;
    }
}
