package com.example.appchat.actor.supervisor;

import akka.actor.*;
import akka.japi.Function;
import akka.japi.pf.ReceiveBuilder;
import akka.serialization.Serialization;
import com.example.appchat.actor.conversation.ConversationCommand;
import com.example.appchat.model.dto.MessageKafka;
import com.example.appchat.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

import java.util.Optional;

import static com.example.appchat.actor.common.SpringExtension.SPRING_EXTENSION_PROVIDER;
import static com.example.appchat.config.ActorNameConfig.ACTOR_CONVERSATION_BEAN_NAME;
import static com.example.appchat.config.ActorNameConfig.USER_ACTOR_BEAN_NAME;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class SupervisorActor extends AbstractActor {
    private ActorRef sender;
    private final ActorSystem actorSystem;
    private final RedisUtil redisUtil;
    private static final SupervisorStrategy strategy =
            new OneForOneStrategy(10, Duration.create("1 minute"),
                    new Function<Throwable, SupervisorStrategy.Directive>() {
                        @Override
                        public SupervisorStrategy.Directive apply(Throwable t) {
                            if (t instanceof ArithmeticException) {
                                log.info("SupervisorActor resume child actor");
                                return SupervisorStrategy.resume();
                            } else if (t instanceof NullPointerException) {
                                log.info("SupervisorActor restart child actor");
                                return SupervisorStrategy.restart();
                            } else if (t instanceof IllegalArgumentException) {
                                log.info("SupervisorActor stop child actor");
                                return SupervisorStrategy.stop();
                            } else {
                                return SupervisorStrategy.escalate();
                            }
                        }
                    });

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }
    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(SupervisorCommand.CreateConversationActor.class, create -> {
                    sender = sender();
                    ActorRef conversationActor;
                    Optional<ActorRef> optChildActor = getContext().findChild(create.message.getConversationId());
                    conversationActor = optChildActor.orElseGet(() -> createConversationActor(create.message.getConversationId()));
                    try {
                        conversationActor.tell(new NullPointerException(), ActorRef.noSender());
                        log.info("Sent test message to conversation actor: " + conversationActor.path());
                    }catch (Exception e){
                        log.error("Error test when conversation actor:" + conversationActor.path() + " send message " + ", because: " + e.getMessage());
                    }
                })
                .match(SupervisorCommand.ForwardPrivateMessage.class, forward -> {
                    sender = sender();
                    ActorRef childActor = getConversationActor(forward.message);
                    try {
                        childActor.tell(new ConversationCommand.SendToPrivateChat(forward.message), childActor);
                        log.info("Sent message to conversation actor: " + childActor.path());
                    }catch (Exception e){
                        log.error("Error when conversation actor:" + childActor.path() + " send message " + ", because: " + e.getMessage());
                    }
                })
                .match(SupervisorCommand.ForwardPublicMessage.class, forward -> {
                    ActorRef childActor = getConversationActor(forward.message);
//                    try {
//                        childActor.tell(new ConversationCommand.SendToPublicChat(forward.message), childActor);
////                        childActor.tell(new NullPointerException(), ActorRef.noSender());
//                        log.info("Sent message to public conversation actor: " + childActor.path());
//                    }catch (Exception e){
//                        log.error("Error when public conversation actor:" + childActor.path() + " send message " + ", because: " + e.getMessage());
//                    }
                })
                .match(SupervisorCommand.UserConnect.class, msg -> {
                    createUserActor(msg.message.getSender());
                })
                .match(SupervisorCommand.UserDisconnect.class, msg -> {
                    Optional<ActorRef> optChildActor = getContext().findChild(msg.message.getSender());
                    optChildActor.ifPresent(actorRef -> getContext().stop(actorRef));
                    redisUtil.delete(msg.message.getSender());
                })
                .match(SupervisorCommand.ForwardMessage.class, forward -> {
                    ActorRef childActor = getConversationActor(forward.message);
                    forwardMessage(forward.message, childActor, getContext());
                })
                .build();
    }

    public void forwardMessage(MessageKafka message, ActorRef actor, ActorContext actorContext){
        switch (message.getStatus()){
            case MESSAGE:
                actor.forward(new ConversationCommand.SendToPublicChat(message), actorContext);
                break;
            case MESSAGE_PRIVATE:
                actor.forward(new ConversationCommand.SendToPrivateChat(message), actorContext);
                break;
        }
    }
     public ActorRef getConversationActor(MessageKafka message) {
         ActorRef childActor;
         Optional<ActorRef> optChildActor = getContext().findChild(message.getConversationId());
         if(!optChildActor.isPresent()){
             childActor = createConversationActor(message.getConversationId());
             log.info("create new actor: " + childActor.path());
         }else {
             childActor = optChildActor.get();
             log.info("already exist actor: " + childActor.path());
         }
         return childActor;
     }

     public ActorRef createConversationActor(String conversationId){
        return getContext().actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem)
                .props(ACTOR_CONVERSATION_BEAN_NAME), conversationId);
     }

    public void createUserActor(String username){
        ActorRef actorRef = getContext().actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem)
                .props(USER_ACTOR_BEAN_NAME), username);
//        redisUtil.save(username, actorRef);
//        String identifier = Serialization.serializedActorPath(actorRef);
//        log.info("user actor: "+ actorRef.path());
    }
}
