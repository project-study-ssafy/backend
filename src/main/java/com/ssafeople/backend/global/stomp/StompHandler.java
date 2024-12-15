package com.ssafeople.backend.global.stomp;

import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.exception.chatting.UserNotLoggedInException;
import com.ssafeople.backend.global.utils.user.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final UserUtils userUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        log.info("accessor : {}", accessor);

        if (accessor.getCommand() == StompCommand.SUBSCRIBE) {

            String destination = accessor.getDestination();
            String substring = destination.substring(destination.lastIndexOf("/") + 1);

            Short roomId = Short.parseShort(substring);
            log.info("destination: {}", destination);

            if (roomId == 2) {
                try {
                    User user = userUtils.getCurrentUser();
                    log.info("User {} subscribed to room {}", user.getId(), roomId);
                } catch (Exception e) {
                    log.error("Unauthorized access attempt to room {}", roomId);
                    throw UserNotLoggedInException.EXCEPTION;
                }
            }
        }
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
