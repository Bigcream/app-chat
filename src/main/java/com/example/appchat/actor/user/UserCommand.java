package com.example.appchat.actor.user;


import com.example.appchat.model.dto.MessageKafka;

public class UserCommand {
    public static final class privateChat {
        public final MessageKafka message;
        public privateChat(MessageKafka message) {
            this.message = message;
        }
    }
    public static final class publicChat {
        public final MessageKafka message;
        public publicChat(MessageKafka message) {
            this.message = message;
        }
    }
    public static final class GetAllRoomAvailable  {}
}