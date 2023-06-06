package com.example.appchat.actor.user;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import com.example.appchat.model.dto.MessageKafka;
import com.example.appchat.model.entity.UserEntity;
import com.example.appchat.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static com.example.appchat.constant.Destination.PRIVATE_CHANNEL;
import static com.example.appchat.constant.Destination.PUBLIC_CHANNEL;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class UserActor extends AbstractActor {
    private ActorRef sender;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepo userRepo;
    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(UserCommand.publicChat.class, msg -> {
                    sendToPublicChat(msg.message);
                    log.info("Sent message to public chat:  " + msg.message.getConversationId());
                })
                .match(UserCommand.privateChat.class, msg ->{
                    sendToPrivateChat(msg.message);
                })
                .build();
    }

    private void sendToPrivateChat(MessageKafka msg) {
        simpMessagingTemplate.convertAndSendToUser(msg.getReceiver(), PRIVATE_CHANNEL, msg);
    }

    @NewSpan
    private void sendToPublicChat(MessageKafka msg) {
        simpMessagingTemplate.convertAndSend(PUBLIC_CHANNEL, msg);
    }
    private UserEntity saveUser(UserEntity user){
        return userRepo.save(user);
    }
}
