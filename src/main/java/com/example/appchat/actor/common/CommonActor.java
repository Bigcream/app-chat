package com.example.appchat.actor.common;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import com.example.appchat.actor.conversation.ConversationCommand;
import com.example.appchat.model.dto.ChatRoomDTO;
import com.example.appchat.model.entity.ChatRoomEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class CommonActor extends AbstractActor {
    private ActorRef sender;
    private final HashMap<Long, List<ChatRoomEntity>> chatRoomMap;
    private final SimpUserRegistry simpUserRegistry;
    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(ConversationCommand.GetAllRoomAvailable.class, msg -> {
                    sender = sender();
                    List<ChatRoomDTO> chatRoomDTOs = getAllRoomAvailable();
                    sender.tell(chatRoomDTOs, self());
                    System.out.println("sent room");
                })
                .match(ConversationCommand.GetAllUserOnline.class, msg ->{
                    sender = sender();
                    List<String> users = getAllUserOnline();
                    sender.tell(users, self());
                    System.out.println("sent user");
                })
                .build();
    }
    public List<ChatRoomDTO> getAllRoomAvailable(){
        List<ChatRoomDTO> chatRoomDTOS = new ArrayList<>();
        chatRoomMap.forEach((aLong, chatRoomEntities) -> chatRoomEntities.forEach(chatRoom ->{
            chatRoomDTOS.add(chatRoom.convertToDTO());
        }));
        return chatRoomDTOS;
    }
    public List<String> getAllUserOnline(){
        List<String> usernames = new ArrayList<>();
        System.out.println(simpUserRegistry.getUserCount());
        Set<SimpUser> simpUsers = simpUserRegistry.getUsers();
        simpUsers.forEach(simpUser -> usernames.add(simpUser.getName()));
        return usernames;
    }
}
