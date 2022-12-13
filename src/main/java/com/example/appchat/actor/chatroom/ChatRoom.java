package com.example.appchat.actor.chatroom;


import com.example.appchat.model.dto.Message;

public class ChatRoom {
    public static final class CreateRoom {
        public final Message message;
        public CreateRoom(Message message) {
            this.message = message;
        }
    }
    public static final class SendPrivateChat {
        public final Message message;
        public SendPrivateChat(Message message) {
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