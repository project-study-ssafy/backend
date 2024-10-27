package com.ssafeople.backend.domain.user.domain;

import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.user.domain.vo.UserInfoVo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Short id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)  // 유니크 설정
    private String email;

    private String passwordHash;

    @Column(nullable = false, unique = true)  // 유니크 설정
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Post> posts = new ArrayList<>();

    public User(String username, String email, String passwordHash, String nickname) {
        this.username = username;
        this.email = email;
        this.role = Role.USER;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UserInfoVo getUserInfo() {
        return UserInfoVo.builder()
            .id(id)
            .username(username)
            .nickname(nickname)
            .email(email)
            .role(role.getValue())
            .build();
    }
}

