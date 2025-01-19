package com.ssafeople.backend.domain.notification.presentation;

import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.utils.notify.NotifyUtils;
import com.ssafeople.backend.global.utils.user.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notify")
public class NotificationController {
    private final UserUtils userUtils;
    private final NotifyUtils notifyUtils;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(
            //@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
    ) {
        User user = userUtils.getCurrentUser();

        SseEmitter emitter = notifyUtils.createEmitter(user.getId());

        new Thread(() -> {
            try {
                while (true) {
                    // 5초마다 Keep-Alive 메시지를 보냄
                    emitter.send(SseEmitter.event().data("Keep-Alive"));
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }
}
