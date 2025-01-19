package com.ssafeople.backend.global.utils.notify;

import com.ssafeople.backend.domain.post.domain.Post;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotifyUtils {

    void sendNotification(Post post);

    SseEmitter createEmitter(Short id);

    void deleteEmitter(Short id);
}
