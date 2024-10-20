package com.ssafepole.backend.domain.user.domain.repository;

import com.ssafepole.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Short> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
