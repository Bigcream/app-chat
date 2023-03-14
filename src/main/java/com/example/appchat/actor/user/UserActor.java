package com.example.appchat.actor.user;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import com.example.appchat.actor.conversation.ConversationCommand;
import com.example.appchat.model.dto.ChatMessage;
import com.example.appchat.model.entity.UserEntity;
import com.example.appchat.repository.UserRepo;
import com.kafkaservice.payload.MessageKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

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
                .match(ChatMessage.class, msg -> {
                    sender = sender();
                    MessageKafka response = chat(msg);
                    sender.tell(response, self());
                })
                .match(ConversationCommand.SendToPrivateChat.class, msg ->{
                    sender = sender();
                    sendToPrivateChat(msg.message);
                })
                .match(UserEntity.class, user -> {
                    sender = sender();
                    UserEntity response = saveUser(user);
                    sender.tell(response, self());
                })
                .build();
    }
    private MessageKafka chat(ChatMessage msg) {
        simpMessagingTemplate.convertAndSend("/chatroom/public", msg.getMessage());
        return msg.getMessage();
    }

    private void sendToPrivateChat(MessageKafka msg) {
        simpMessagingTemplate.convertAndSendToUser(msg.getReceiver(),"/private",msg);
        System.out.println("sender private " + sender.path());
    }
    private UserEntity saveUser(UserEntity user){
        return userRepo.save(user);
    }
}
