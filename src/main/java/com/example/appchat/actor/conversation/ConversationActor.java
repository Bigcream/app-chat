package com.example.appchat.actor.conversation;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.japi.pf.ReceiveBuilder;
import com.example.appchat.actor.user.UserCommand;
import com.example.appchat.model.dto.MessageKafka;
import com.example.appchat.model.entity.ChatRoomEntity;
import com.example.appchat.repository.ChatRoomRepo;
import com.example.appchat.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static akka.pattern.Patterns.ask;

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
                .match(ConversationCommand.publicChat.class, msg -> {
                    ActorRef userActor = msg.userActor;
                    sender = sender();
                    try {
                        userActor.tell(new UserCommand.publicChat(msg.message), sender);
                        log.info("Sent message to user actor");
                    }catch (Exception e){
                        log.error("Error when send message to public chat:" + msg.message.getConversationId() + ", because: " + e.getMessage());
                        sender.tell(new ConversationCommand.publicChat(msg.message, sender), self());
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
                .match(ConversationCommand.privateChat.class, msg ->{
                    sender = sender();
                    ActorRef userActor = msg.userActor;
                    try {
                        userActor.tell(new UserCommand.privateChat(msg.message), sender);
                        log.info("Sent message to private user:  " + msg.message.getReceiver());
                    }catch (Exception e){
                        log.error("Error when send message to private user:" + msg.message.getReceiver() + ", because: " + e.getMessage());
                        sender.tell(new ConversationCommand.privateChat(msg.message, userActor), self());
                        log.info("Retry send message to private user success");
                    }
                })
                .build();
    }

    private void onCreateConversation(MessageKafka msg) {
        ChatRoomEntity chatRoom = new ChatRoomEntity();
        chatRoomRepo.save(chatRoom);
    }
}

