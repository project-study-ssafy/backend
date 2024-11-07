package com.ssafeople.backend.domain.comment.domain;

import com.ssafeople.backend.domain.post.domain.Post;
import jakarta.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private String id;

}
