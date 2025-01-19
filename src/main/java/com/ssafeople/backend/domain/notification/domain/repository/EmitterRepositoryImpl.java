package com.ssafeople.backend.domain.notification.domain.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository {

    private final ConcurrentMap<Short, SseEmitter> emitters = new ConcurrentHashMap<>();

    private SseEmitter makeConnection(Short userId) {
        SseEmitter emitter = new SseEmitter();
        emitters.put(userId, emitter);
        try {
            emitter.send("1");
        } catch (Exception e){
            System.out.println("여기 들어와도 아무 의미 없음");
        }
        return emitter;
    }

    public SseEmitter getEmitter(Short userId) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter == null) {
            emitter = makeConnection(userId);
        }
        return emitter;
    }

    public void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }

}
