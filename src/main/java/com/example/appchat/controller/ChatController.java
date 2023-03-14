package com.example.appchat.controller;

import akka.actor.ActorRef;
import com.example.appchat.actor.conversation.ConversationCommand;
import com.example.appchat.constant.RedisKeyPattern;
import com.example.appchat.model.dto.ChatRoomDTO;
import com.example.appchat.service.ChatRoomService;
import com.example.appchat.service.UserChatService;
import com.example.appchat.util.ActorUtil;
import com.example.appchat.util.RedisUtil;
import com.kafkaservice.payload.MessageKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appchat")
public class ChatController extends BaseController {
    private final ActorRef actorCommon;
    private final ChatRoomService chatRoomService;
    private final UserChatService userChatService;
    private final RedisUtil redisUtil;

    @PostMapping("/send-to-public-chat")
    public MessageKafka sendToPublicChat(@RequestBody MessageKafka message) throws Exception {
        userChatService.sendPublicChat(message);
        return message;
    }

    @PostMapping("/create-conversation")
    public MessageKafka createConversationActor(@RequestBody MessageKafka message) throws Exception {
        userChatService.createConversationActor(message);
        return message;
    }

//    @MessageMapping("/create-room")
//    public String userCreateRoom(@Payload MessageKafka message) throws Exception {
//        return chatRoomService.createRoom(message);
//    }

    @PostMapping("/send-to-private-chat")
    public MessageKafka sendToPrivateChat(@RequestBody MessageKafka message) throws Exception {
        userChatService.sendPrivateChat(message);
        return message;
    }
    @GetMapping("/room-available")
    public List<ChatRoomDTO> getAllRoomAvailable(){
        return ActorUtil.askObject(actorCommon, new ConversationCommand.GetAllRoomAvailable(), ChatRoomDTO.class);
    }
    @GetMapping("/leave-room/{roomId}")
    public ResponseEntity<Void> leaveRoom(@PathVariable Long roomId, @RequestParam String username){
        return new  ResponseEntity<>(chatRoomService.leaveRoom(roomId, username), noCacheHeader, HttpStatus.OK);
    }
    @GetMapping("/all-user-online")
    public List<Object> getAllUserOnline(){
        return redisUtil.findAllByKeyPattern(RedisKeyPattern.USER_ONlINE + RedisKeyPattern.ASTERISK);
    }
}
