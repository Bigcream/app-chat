package com.example.appchat.actor.supervisor;


import com.example.appchat.model.dto.MessageKafka;
import com.example.appchat.model.dto.UserDTO;

public class SupervisorCommand {
    public static final class CreateConversationActor {
        public final MessageKafka message;
        public CreateConversationActor(MessageKafka message) {
            this.message = message;
        }
    }

    public static final class UserConnect {
        public final UserDTO userDTO;
        public UserConnect(UserDTO userDTO) {
            this.userDTO = userDTO;
        }
    }

    public static final class UserDisconnect {
        public final UserDTO userDTO;
        public UserDisconnect(UserDTO userDTO) {
            this.userDTO = userDTO;
        }
    }

    public static final class ForwardMessage {
        public final MessageKafka message;
        public ForwardMessage(MessageKafka message) {
            this.message = message;
        }
    }
}
