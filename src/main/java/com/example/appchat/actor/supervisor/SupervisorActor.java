package com.example.appchat.actor.supervisor;

import akka.actor.*;
import akka.japi.Function;
import akka.japi.pf.ReceiveBuilder;
import com.example.appchat.actor.conversation.ConversationCommand;
import com.example.appchat.constant.ActorName;
import com.example.appchat.util.ActorUtil;
import com.kafkaservice.payload.MessageKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

import java.util.Optional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class SupervisorActor extends AbstractActor {
    private ActorRef sender;
    private final ActorSystem actorSystem;
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
//                    try {
//                        ActorRef actorRef = ActorUtil.getInstanceOfChildActor(getContext(), create.message.getConversationId(), actorSystem, ActorName.ACTOR_CONVERSATION_BEAN_NAME);
//                        log.info("Conversation created" + actorRef.path());
//                    }catch (Exception e){
//                        log.error("Error when create actor " + create.message.getConversationId() + ", because: " + e.getMessage());
//                    }
                    sender = sender();
                    ActorRef childActor;
                    Optional<ActorRef> optChildActor = getContext().findChild(create.message.getConversationId());
                    if(!optChildActor.isPresent()){
                        childActor = ActorUtil.getInstanceOfChildActor(getContext(), create.message.getConversationId(), actorSystem, ActorName.ACTOR_CONVERSATION_BEAN_NAME);
                    }else {
                        childActor = optChildActor.get();
                    }
                    try {
                        childActor.tell(new NullPointerException(), ActorRef.noSender());
                        log.info("Sent test message to conversation actor:  " + childActor.path());
                    }catch (Exception e){
                        log.error("Error test when conversation actor:" + childActor.path() + " send message " + ", because: " + e.getMessage());
                    }
                })
                .match(SupervisorCommand.ForwardPrivateMessage.class, forward -> {
                    sender = sender();
//                    log.info("Total child restart" + getContext().getChildren());
                    ActorRef childActor = getChildActor(forward.message);
                    try {
                        childActor.tell(new ConversationCommand.SendToPrivateChat(forward.message), childActor);
//                        childActor.tell(new NullPointerException(), ActorRef.noSender());
                        log.info("Sent message to conversation actor:  " + childActor.path());
                    }catch (Exception e){
                        log.error("Error when conversation actor:" + childActor.path() + " send message " + ", because: " + e.getMessage());
                    }
                })
                .match(SupervisorCommand.ForwardPublicMessage.class, forward -> {
                    ActorRef childActor = getChildActor(forward.message);
                    try {
                        childActor.tell(new ConversationCommand.SendToPublicChat(forward.message), childActor);
//                        childActor.tell(new NullPointerException(), ActorRef.noSender());
                        log.info("Sent message to public conversation actor:  " + childActor.path());
                    }catch (Exception e){
                        log.error("Error when public conversation actor:" + childActor.path() + " send message " + ", because: " + e.getMessage());
                    }
                })
                .build();
    }

     public ActorRef getChildActor(MessageKafka message) throws Exception {
         ActorRef childActor;
         Optional<ActorRef> optChildActor = getContext().findChild(message.getConversationId());
         if(!optChildActor.isPresent()){
             childActor = ActorUtil.getInstanceOfChildActor(getContext(), message.getConversationId(), actorSystem, ActorName.ACTOR_CONVERSATION_BEAN_NAME);
             log.info("create new actor: " + childActor.path());

         }else {
             childActor = optChildActor.get();
             log.info("already exist actor: " + childActor.path());
         }
         return childActor;
     }
}
