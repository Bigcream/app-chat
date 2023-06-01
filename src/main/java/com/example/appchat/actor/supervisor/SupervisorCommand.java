package com.example.appchat.actor.supervisor;


import com.example.appchat.model.dto.MessageKafka;

public class SupervisorCommand {
    public static final class CreateConversationActor {
        public final MessageKafka message;
        public CreateConversationActor(MessageKafka message) {
            this.message = message;
        }
    }

    public static final class UserConnect {
        public final MessageKafka message;
        public UserConnect(MessageKafka message) {
            this.message = message;
        }
    }

    public static final class UserDisconnect {
        public final MessageKafka message;
        public UserDisconnect(MessageKafka message) {
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

    public static final class ForwardMessage {
        public final MessageKafka message;
        public ForwardMessage(MessageKafka message) {
            this.message = message;
        }
    }
}
