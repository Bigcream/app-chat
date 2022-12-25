package com.example.appchat.util;

import akka.actor.*;
import akka.pattern.AskableActorSelection;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.example.appchat.constant.ActorName;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.appchat.actor.common.SpringExtension.SPRING_EXTENSION_PROVIDER;


public class ActorUtil {

    public static ActorRef getInstanceOfActor(String name, ActorSystem actorSystem, String actorName) throws Exception {
        ActorRef instanceOfActor;
        ActorSelection sel = actorSystem.actorSelection(ActorName.URL_AKKA_ACTOR + name);
        AskableActorSelection asker = new AskableActorSelection(sel);
        Future<Object> future = asker.ask(new Identify(1), new Timeout(5,
                TimeUnit.SECONDS));
        ActorIdentity identity = (ActorIdentity) Await.result(future, Duration.apply(5, "seconds"));
        Optional<ActorRef> reference = identity.getActorRef();
        // Actor exists
        // Actor does not exits
        instanceOfActor = reference.orElseGet(() -> actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem)
                .props(actorName), name));
        return instanceOfActor;
    }

    public static  <T> T ask(ActorRef actor, Object msg, Class<T> returnTypeClass){
        return  (T) Patterns.ask(actor, msg, java.time.Duration.ofMillis(2000)).toCompletableFuture().join();
    }

    public static  <T> T askObject(ActorRef actor, Object msg, Object returnTypeClass){
        return  (T) Patterns.ask(actor, msg, java.time.Duration.ofMillis(2000)).toCompletableFuture().join();
    }

//    public static ActorRef getUserActor(String name, ActorSystem actorSystem){
//        ActorRef[] userActor = {null};
//        ActorSelection sel = actorSystem.actorSelection("akka://akka-spring-demo/user/" + name);
//        Future<ActorRef> future = sel.resolveOne(new Timeout(5,
//                TimeUnit.SECONDS));
//// Wait for the completion of task to be completed.
//        future.onComplete(new OnComplete<ActorRef>() {
//            @Override
//            public void onComplete(Throwable excp, ActorRef child)
//                    throws Throwable {
//                // ActorNotFound will be the Throwable if actor not exists
//                if (excp != null) {
//                    userActor[0] = actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem)
//                            .props("userActor"), name);
//                    // Actor does not exists
//                } else {
//                    userActor[0] = child;
//                    // Actor exits
//                }
//            }
//        }, actorSystem.dispatcher());
//        return userActor[0];
//    }
}
