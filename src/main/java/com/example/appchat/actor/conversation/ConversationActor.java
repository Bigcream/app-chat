package com.example.appchat.actor.conversation;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import com.example.appchat.constant.Destination;
import com.example.appchat.model.dto.MessageKafka;
import com.example.appchat.model.entity.ChatRoomEntity;
import com.example.appchat.repository.ChatRoomRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class ConversationActor extends AbstractActor {
    private ActorRef sender;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRoomRepo chatRoomRepo;
    private static final HashMap<Long, Long> chatRoomIdMap = new HashMap<>();
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ConversationCommand.SendToPublicChat.class, msg -> {
                    sender = sender();
                    sendToPublicChat(msg.message);
//                    redisTemplate.opsForValue().set(response.getRoomId().toString(), response);
                })
                .match(ConversationCommand.Create.class, create -> {
                    sender = sender();
                    onCreateConversation(create.message);
                    System.out.println("Conversation created");
                })
                .match(
                        Exception.class,
                        exception -> {
                            throw exception;
                        })
                .match(ConversationCommand.SendToPrivateChat.class, msg ->{
                    sender = sender();
                    try {
                        sendToPrivateChat(msg.message);
                        log.info("Sent message to private user:  " + msg.message.getReceiver());
                    }catch (Exception e){
                        log.error("Error when send message to private user:" + msg.message.getReceiver() + ", because: " + e.getMessage());
                        sender.tell(new ConversationCommand.SendToPrivateChat(msg.message), self());
                        log.info("Retry send message to private user success");
                    }
                })
                .build();
    }

    private void onCreateConversation(MessageKafka msg) {
        ChatRoomEntity chatRoom = new ChatRoomEntity();
//        chatRoom.setChatRoomId(msg.getRoomId());
        chatRoomRepo.save(chatRoom);
    }
    private void sendToPublicChat(MessageKafka msg) {
        simpMessagingTemplate.convertAndSend(Destination.PUBLIC_CHANNEL, msg);
    }

    private void sendToPrivateChat(MessageKafka msg) {
        simpMessagingTemplate.convertAndSendToUser(msg.getReceiver(), Destination.PRIVATE_CHANNEL, msg);
    }
}

