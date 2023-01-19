package com.example.appchat.config;


import com.example.appchat.model.dto.UserDTO;
import com.example.appchat.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Map;

@RequiredArgsConstructor
public class UserInterceptor implements ChannelInterceptor {

    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        UserDTO userDTO = new UserDTO();
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            if (raw instanceof Map) {
                Object name = ((Map<?, ?>) raw).get("username");
                accessor.setUser(new User(name.toString()));
                userDTO.setUsername(name.toString());
            }
            Object rawSimpSessionId = message.getHeaders().get(SimpMessageHeaderAccessor.SESSION_ID_HEADER);
            if (rawSimpSessionId instanceof Map) {
                Object simpSessionId = ((Map<?, ?>) raw).get("simpSessionId");
                userDTO.setSessionId(simpSessionId.toString());
            }
            redisTemplate.opsForValue().set(userDTO.getSessionId(), userDTO);
        }

        if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            redisTemplate.delete(userDTO.getSessionId());
        }
        return message;
    }
}
