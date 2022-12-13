package com.example.appchat.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.example.appchat.actor.chatroom.ChatRoom;
import com.example.appchat.constant.ActorName;
import com.example.appchat.model.dto.ChatMessage;
import com.example.appchat.model.dto.Message;
import com.example.appchat.model.entity.UserEntity;
import com.example.appchat.repository.IUserRepository;
import com.example.appchat.utility.UtilityActor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class UserChatService {
    private final IUserRepository userRepo;
    private final ActorSystem actorSystem;
    private final HashMap<String, AtomicLong> mapId;
    private final PasswordEncoder passwordEncoder;
    public void joinRoom(Message message) throws Exception {
        ActorRef userActor = UtilityActor.getInstanceOfActor(message.getSenderName(), actorSystem, ActorName.USER_ACTOR);
        userActor.tell(new ChatMessage(message), userActor);
        System.out.println("join " + userActor.path());
    }
    public void sendPublicChat(Message message) throws Exception {
        ActorRef userActor = UtilityActor.getInstanceOfActor(message.getSenderName(), actorSystem, ActorName.USER_ACTOR);
        userActor.tell(new ChatMessage(message), userActor);
        System.out.println("test " + userActor.path());
    }
    public void sendPrivateChat(Message message) throws Exception{
        ActorRef userActor = UtilityActor.getInstanceOfActor(message.getSenderName(), actorSystem, ActorName.USER_ACTOR);
        userActor.tell(new ChatRoom.SendPrivateChat(message), userActor);
    }
    public UserEntity register(UserEntity user) throws Exception {
        UserEntity user1 = new UserEntity();
        mapId.get("userId").getAndIncrement();
        ActorRef userActor = UtilityActor.getInstanceOfActor(user.getUsername(), actorSystem, ActorName.USER_ACTOR);
        Long id = mapId.get("userId").get();
        user.setId(id);
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("id: " + user1.getId());
        System.out.println("actor name: " + userActor.path());
        user1 = UtilityActor.ask(userActor, UserEntity.builder().id(user.getId()).username(user.getUsername()).password(user.getPassword()).build(), UserEntity.class);
        return user1;
    }
}
