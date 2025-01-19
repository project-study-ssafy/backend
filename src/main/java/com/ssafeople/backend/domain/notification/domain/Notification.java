package com.ssafeople.backend.domain.notification.domain;

import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.database.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "notifications")
@Entity
@Getter
@NoArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "is_read")
    private boolean isRead;

    public void markAsRead() {
        this.isRead = true;
    }

    public Notification(String content, Post post, User user) {
        this.content = content;
        this.post = post;
        this.user = user;
        this.isRead = false;
    }
}
