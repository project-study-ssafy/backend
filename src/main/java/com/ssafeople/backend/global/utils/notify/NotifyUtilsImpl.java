package com.ssafeople.backend.global.utils.notify;

import com.ssafeople.backend.domain.notification.domain.Notification;
import com.ssafeople.backend.domain.notification.domain.repository.EmitterRepository;
import com.ssafeople.backend.domain.notification.domain.repository.NotificationRepository;
import com.ssafeople.backend.domain.notification.presentation.dto.NotificationDto;
import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.global.exception.notify.NotificationNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyUtilsImpl implements NotifyUtils {

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;

    public SseEmitter createEmitter(Short userId) {
        return emitterRepository.getEmitter(userId);
    }

    public void deleteEmitter(Short userId) {
        emitterRepository.removeEmitter(emitterRepository.getEmitter(userId));
    }

    public void sendNotification(Post post) {
        SseEmitter emitter = emitterRepository.getEmitter(post.getUser().getId());
        log.info("emitter:{}", emitter==null?"null":emitter);
        log.info("User Id : {}", post.getUser().getId());

        if (emitter == null) {
            log.warn("No emitter found for user: {}", post.getUser().getId());
            return;
        }

        Notification notification = new Notification("새로운 댓글이 작성되었습니다.", post, post.getUser());

        try {
            log.info("notification : {}", notification);
            notificationRepository.save(notification);
            emitter.send(toDto(notification));
        } catch (IOException e) {
            log.info("Fail to Send Notification: {}", notification);
            emitterRepository.removeEmitter(emitter);
        } catch (AsyncRequestTimeoutException e) {
            log.info("Describe Connection is Closed");
        } catch (NullPointerException e) {
            log.info("NullPointer Exception Found");
        } catch (Exception e) {
            log.info("ㅋㅋㅋㅋㅋㅋㅋ");
        }
    }

    public void markAsRead(Long notificationId) {
        Notification notify = notificationRepository.findById(notificationId).orElse(null);
        if (notify == null) {
            throw NotificationNotExistException.Exception;
        }
        notify.markAsRead();
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notificationRepository::delete);
    }

    private NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .userId(notification.getUser().getId())
                .postId(notification.getPost().getId())
                .isRead(notification.isRead())
                .build();
    }
}
