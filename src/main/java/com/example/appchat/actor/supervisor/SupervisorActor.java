package com.example.appchat.actor.supervisor;

import akka.actor.*;
import akka.japi.Function;
import akka.japi.pf.ReceiveBuilder;
import com.example.appchat.actor.conversation.ConversationActor;
import com.example.appchat.actor.conversation.ConversationCommand;
import com.example.appchat.constant.ActorName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

import java.util.Optional;

import static com.example.appchat.actor.common.SpringExtension.SPRING_EXTENSION_PROVIDER;

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
                                return SupervisorStrategy.resume();
                            } else if (t instanceof NullPointerException) {
                                return SupervisorStrategy.restart();
                            } else if (t instanceof IllegalArgumentException) {
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
                    getContext().actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem)
                            .props("conversationActor"), create.message.getConversationId());
                    System.out.println("Conversation created");
                })
                .match(SupervisorCommand.ForwardMessage.class, forward -> {
                    sender = sender();
                    Optional<ActorRef> childActor = getContext().findChild(forward.message.getConversationId());
                    if(childActor.isPresent()){
                        childActor.get().tell(new ConversationCommand.SendToPrivateChat(forward.message), childActor.get());
                        System.out.println("Conversation sent" + childActor.get().path());
                    }
                })
                .build();
    }
}
