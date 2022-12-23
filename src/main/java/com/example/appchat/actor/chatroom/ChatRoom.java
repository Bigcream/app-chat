package com.example.appchat.actor.chatroom;


import com.example.appchat.model.dto.MessageKafka;

public class ChatRoom {
    public static final class CreateRoom {
        public final MessageKafka message;
        public CreateRoom(MessageKafka message) {
            this.message = message;
        }
    }
    public static final class SendPrivateChat {
        public final MessageKafka message;
        public SendPrivateChat(MessageKafka message) {
            this.message = message;
        }
    }
    public static final class GetAllRoomAvailable  {}
    public static final class GetAllUserOnline { }
    public static final class MessagePosted {
        public final String screenName;
        public final String message;

        public MessagePosted(String screenName, String message) {
            this.screenName = screenName;
            this.message = message;
        }
    }
    public static final class PostMessage{
        public final String message;

        public PostMessage(String message) {
            this.message = message;
        }
    }

    static final class NotifyClient{
        final MessagePosted message;
        public NotifyClient(MessagePosted message) {
            this.message = message;
        }
    }
}