package com.example.appchat.actor.conversation;


import akka.actor.ActorRef;
import com.example.appchat.model.dto.MessageKafka;

public class ConversationCommand {
    public static final class Create {
        public final MessageKafka message;
        public Create(MessageKafka message) {
            this.message = message;
        }
    }
    public static final class privateChat {
        public final MessageKafka message;
        public final ActorRef userActor;
        public privateChat(MessageKafka message, ActorRef userActor) {
            this.message = message;
            this.userActor = userActor;
        }
    }
    public static final class publicChat {
        public final MessageKafka message;
        public final ActorRef userActor;
        public publicChat(MessageKafka message, ActorRef userActor) {
            this.message = message;
            this.userActor = userActor;
        }
    }
}