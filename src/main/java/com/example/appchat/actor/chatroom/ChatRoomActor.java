package com.example.appchat.actor.chatroom;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import com.example.appchat.model.dto.ChatMessage;
import com.example.appchat.model.dto.Message;
import com.example.appchat.model.entity.ChatRoomEntity;
import com.example.appchat.service.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ChatRoomActor extends AbstractActor {
    private ActorRef sender;
    private final GreetingService greetingService;
    private final IChatRoomRepository chatRoomRepo;
    private static final HashMap<Long, Long> chatRoomIdMap = new HashMap<>();
    private final RedisTemplate<String, Object> redisTemplate;

    public ChatRoomActor(GreetingService greetingService, IChatRoomRepository chatRoomRepo, RedisTemplate<String, Object> redisTemplate) {
        this.greetingService = greetingService;
        this.chatRoomRepo = chatRoomRepo;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ChatMessage.class, msg -> {
                    sender = sender();
                    Message response = chat(msg);
                    sender.tell(response, self());
                    redisTemplate.opsForValue().set(response.getRoomId().toString(), response);
                })
                .match(ChatRoom.CreateRoom.class, createRoom -> {
                    sender = sender();
                    onCreateRoom(createRoom.message);
                    sender.tell("Room created", self());
                    System.out.println("Room created");
                })
                .build();
    }
    private Message chat(ChatMessage msg) {
        return msg.getMessage();
    }

    private void onCreateRoom(Message msg) {
        ChatRoomEntity chatRoom = new ChatRoomEntity();
        chatRoom.setChatRoomId(msg.getRoomId());
        chatRoomRepo.save(chatRoom);
    }
}

