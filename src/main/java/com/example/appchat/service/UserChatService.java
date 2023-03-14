package com.example.appchat.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.example.appchat.actor.supervisor.SupervisorCommand;
import com.example.appchat.constant.ActorName;
import com.example.appchat.model.dto.ChatMessage;
import com.example.appchat.repository.UserRepo;
import com.example.appchat.util.ActorUtil;
import com.kafkaservice.payload.MessageKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class UserChatService {
    private final UserRepo userRepo;
    private final ActorSystem actorSystem;
    private final ActorRef actorSupervisor;
    private final HashMap<String, AtomicLong> mapId;
    public void joinRoom(MessageKafka message) throws Exception {
        ActorRef userActor = ActorUtil.getInstanceOfActor(message.getSender(), actorSystem, ActorName.USER_ACTOR);
        userActor.tell(new ChatMessage(message), userActor);
        System.out.println("join " + userActor.path());
    }
    public void sendPublicChat(MessageKafka message) {
//        actorSupervisor.tell(new SupervisorCommand.ForwardMessage(message), actorSupervisor);
        actorSupervisor.tell(new SupervisorCommand.ForwardPublicMessage(message), actorSupervisor);
    }
    public void createConversationActor(MessageKafka message) throws Exception{
//        ActorRef userActor = ActorUtil.getInstanceOfActor(message.getSender(), actorSystem, ActorName.USER_ACTOR);
//        userActor.tell(new ConversationCommand.SendToPrivateChat(message), userActor);
        actorSupervisor.tell(new SupervisorCommand.CreateConversationActor(message), actorSupervisor);
    }

    public void sendPrivateChat(MessageKafka message) {
//        ActorRef userActor = ActorUtil.getInstanceOfActor(message.getSender(), actorSystem, ActorName.USER_ACTOR);
//        userActor.tell(new ConversationCommand.SendToPrivateChat(message), userActor);
        actorSupervisor.tell(new SupervisorCommand.ForwardPrivateMessage(message), actorSupervisor);
    }
//    public UserEntity register(UserEntity user) throws Exception {
//        UserEntity user1 = new UserEntity();
//        mapId.get("userId").getAndIncrement();
//        ActorRef userActor = UtilityActor.getInstanceOfActor(user.getUsername(), actorSystem, ActorName.USER_ACTOR);
//        Long id = mapId.get("userId").get();
//        user.setId(id);
//        user.setUsername(user.getUsername());
//        System.out.println("id: " + user1.getId());
//        System.out.println("actor name: " + userActor.path());
//        user1 = UtilityActor.ask(userActor, UserEntity.builder().id(user.getId()).username(user.getUsername()).password(user.getPassword()).build(), UserEntity.class);
//        return user1;
//    }
}
