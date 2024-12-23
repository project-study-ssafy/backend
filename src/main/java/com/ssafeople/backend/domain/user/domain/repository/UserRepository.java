package com.ssafeople.backend.domain.user.domain.repository;

import com.ssafeople.backend.domain.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Short> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByChattingNickname(String chattingNickname);
}
