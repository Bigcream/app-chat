package com.example.appchat.actor.supervisor;

import com.kafkaservice.payload.MessageKafka;

public class SupervisorCommand {
    public static final class CreateConversationActor {
        public final MessageKafka message;
        public CreateConversationActor(MessageKafka message) {
            this.message = message;
        }
    }

    public static final class ForwardPrivateMessage {
        public final MessageKafka message;
        public ForwardPrivateMessage(MessageKafka message) {
            this.message = message;
        }
    }

    public static final class ForwardPublicMessage {
        public final MessageKafka message;
        public ForwardPublicMessage(MessageKafka message) {
            this.message = message;
        }
    }
}
