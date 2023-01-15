package com.example.appchat.actor.conversation;


import com.example.appchat.model.dto.MessageKafka;

public class ConversationCommand {
    public static final class Create {
        public final MessageKafka message;
        public Create(MessageKafka message) {
            this.message = message;
        }
    }
    public static final class SendToPrivateChat {
        public final MessageKafka message;
        public SendToPrivateChat(MessageKafka message) {
            this.message = message;
        }
    }
    public static final class SendToPublicChat {
        public final MessageKafka message;
        public SendToPublicChat(MessageKafka message) {
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