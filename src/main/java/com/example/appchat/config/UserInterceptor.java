package com.example.appchat.config;


import akka.actor.ActorRef;
import com.example.appchat.actor.supervisor.SupervisorCommand;
import com.example.appchat.constant.RedisKeyPattern;
import com.example.appchat.model.dto.UserDTO;
import com.example.appchat.model.entity.User;
import com.example.appchat.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserInterceptor implements ChannelInterceptor {

    private final RedisUtil redisUtil;
    private final ActorRef actorSupervisor;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        UserDTO userDTO = new UserDTO();
        Object simpSessionId = message.getHeaders().get(SimpMessageHeaderAccessor.SESSION_ID_HEADER);
        assert simpSessionId != null;
        String userKey = RedisKeyPattern.USER_ONlINE + simpSessionId;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            if (raw instanceof Map) {
                List<String> name = (List<String>) ((Map<?, ?>) raw).get("username");
                accessor.setUser(new User(name.get(0)));
                userDTO.setUsername(name.get(0));
                actorSupervisor.tell(new SupervisorCommand.UserConnect(userDTO), actorSupervisor);
            }
            userDTO.setSessionId(simpSessionId.toString());
            redisUtil.save(userKey, userDTO);
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            List<String> destinations = new ArrayList<>();
            Object destination = message.getHeaders().get(SimpMessageHeaderAccessor.DESTINATION_HEADER);
            userDTO = (UserDTO) redisUtil.findByKey(userKey);
            if(Optional.ofNullable(userDTO.getDestination()).isPresent()){
                destinations = userDTO.getDestination();
            }
            assert destination != null;
            destinations.add(destination.toString());
            userDTO.setDestination(destinations);
            redisUtil.save(userKey, userDTO);
        }

        if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            User user = (User) message.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER);
            assert user != null;
            userDTO.setUsername(user.getName());
            actorSupervisor.tell(new SupervisorCommand.UserDisconnect(userDTO), actorSupervisor);
            redisUtil.delete(userKey);
        }
        return message;
    }
}
