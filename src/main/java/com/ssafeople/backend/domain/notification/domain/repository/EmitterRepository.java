package com.ssafeople.backend.domain.notification.domain.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository{
    SseEmitter getEmitter(Short id);

    void removeEmitter(SseEmitter emitter);
}
