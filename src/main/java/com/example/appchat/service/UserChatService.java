package com.example.appchat.service;

import akka.actor.ActorRef;
import com.example.appchat.actor.supervisor.SupervisorCommand;
import com.example.appchat.model.dto.MessageKafka;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserChatService {
    private final ActorRef actorSupervisor;
    @NewSpan
    public void sendPublicChat(MessageKafka message) {
        actorSupervisor.tell(new SupervisorCommand.ForwardMessage(message), actorSupervisor);
    }
    public void createConversationActor(MessageKafka message) throws Exception{
        actorSupervisor.tell(new SupervisorCommand.CreateConversationActor(message), actorSupervisor);
    }

    public void sendPrivateChat(MessageKafka message) {
        actorSupervisor.tell(new SupervisorCommand.ForwardMessage(message), actorSupervisor);
    }
}
