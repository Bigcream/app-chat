package com.example.appchat.actor.conversation;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.example.appchat.actor.user.UserActor;
import com.example.appchat.actor.user.UserCommand;
import com.example.appchat.config.ActorNameConfig;
import com.example.appchat.constant.Destination;
import com.example.appchat.model.dto.MessageKafka;
import com.example.appchat.model.entity.ChatRoomEntity;
import com.example.appchat.repository.ChatRoomRepo;
import com.example.appchat.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.HashMap;

import static akka.pattern.Patterns.ask;
import static com.example.appchat.constant.Destination.PRIVATE_CHANNEL;
import static com.example.appchat.constant.Destination.PUBLIC_CHANNEL;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class ConversationActor extends AbstractActor {
    private final ActorSystem actorSystem;
    private ActorRef sender;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRoomRepo chatRoomRepo;
    private final RedisUtil redisUtil;

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ConversationCommand.SendToPublicChat.class, msg -> {
                    ActorRef userActor = getContext().actorSelection("/user/actorSupervisor/1").anchor();
                    sender = sender();
//                    ActorRef userActor = (ActorRef) redisUtil.findByKey(msg.message.getSender());
                    try {
                        userActor.tell(new UserCommand.SendToPublicChat(msg.message), sender);
                        log.info("Sent message to user actor");
                    }catch (Exception e){
                        log.error("Error when send message to public chat:" + msg.message.getConversationId() + ", because: " + e.getMessage());
                        sender.tell(new ConversationCommand.SendToPublicChat(msg.message), self());
                        log.info("Retry send message to public user success");
                    }
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
        simpMessagingTemplate.convertAndSend(PUBLIC_CHANNEL, msg);
    }

    private void sendToPrivateChat(MessageKafka msg) {
        simpMessagingTemplate.convertAndSendToUser(msg.getReceiver(), PRIVATE_CHANNEL, msg);
    }
}

