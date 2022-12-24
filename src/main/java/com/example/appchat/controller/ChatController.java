package com.example.appchat.controller;

import akka.actor.ActorRef;
import com.example.appchat.actor.chatroom.ChatRoom;
import com.example.appchat.model.dto.ChatRoomDTO;
import com.example.appchat.model.dto.MessageKafka;
import com.example.appchat.service.ChatRoomService;
import com.example.appchat.service.UserChatService;
import com.example.appchat.util.ActorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController extends BaseController {
    private final ActorRef actorCommon;
    private final ChatRoomService chatRoomService;
    private final UserChatService userChatService;

    @MessageMapping("/message")
    public MessageKafka receiveMessage(@Payload MessageKafka message) throws Exception {
        userChatService.sendPublicChat(message);
        return message;
    }

    @MessageMapping("/join-room")
    public MessageKafka userJoinRoom(@Payload MessageKafka message) throws Exception {
        userChatService.joinRoom(message);
        return message;
    }
//    @MessageMapping("/create-room")
//    public String userCreateRoom(@Payload MessageKafka message) throws Exception {
//        return chatRoomService.createRoom(message);
//    }

    @MessageMapping("/private-message")
    public MessageKafka recMessage(@Payload MessageKafka message) throws Exception {
        userChatService.sendPrivateChat(message);
        return message;
    }
    @GetMapping("/room-available")
    public List<ChatRoomDTO> getAllRoomAvailable(){
        return ActorUtil.askObject(actorCommon, new ChatRoom.GetAllRoomAvailable(), ChatRoomDTO.class);
    }
    @GetMapping("/leave-room/{roomId}")
    public ResponseEntity<Void> leaveRoom(@PathVariable Long roomId, @RequestParam String username){
        return new  ResponseEntity<>(chatRoomService.leaveRoom(roomId, username), noCacheHeader, HttpStatus.OK);
    }
    @GetMapping("/all-user-online")
    public List<String> getAllUserOnline(){
        return ActorUtil.askObject(actorCommon, new ChatRoom.GetAllUserOnline(), String.class);
    }
}
