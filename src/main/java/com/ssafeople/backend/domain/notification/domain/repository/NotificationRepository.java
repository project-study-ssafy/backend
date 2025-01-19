package com.ssafeople.backend.domain.notification.domain.repository;

import com.ssafeople.backend.domain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
