package com.example.appchat.actor.supervisor;

import com.example.appchat.model.dto.MessageKafka;

public class SupervisorCommand {
    public static final class CreateConversationActor {
        public final MessageKafka message;
        public CreateConversationActor(MessageKafka message) {
            this.message = message;
        }
    }

    public static final class ForwardMessage{
        public final MessageKafka message;
        public ForwardMessage(MessageKafka message) {
            this.message = message;
        }
    }
}
