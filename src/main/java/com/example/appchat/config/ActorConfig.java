package com.example.appchat.config;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.example.appchat.constant.ActorName;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.example.appchat.actor.common.SpringExtension.SPRING_EXTENSION_PROVIDER;


@Configuration
@RequiredArgsConstructor
public class ActorConfig {

    private final ApplicationContext applicationContext;

    @Bean
    public Config akkaConfig(){
        return ConfigFactory.load();
    }

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create(ActorName.ACTOR_SYSTEM, akkaConfig());
        SPRING_EXTENSION_PROVIDER.get(system)
                .initialize(applicationContext);
        return system;
    }
    @Bean(name = ActorName.ACTOR_COMMON_NAME)
    public ActorRef create(){
        ActorSystem actorSystem = actorSystem();
        return actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem)
                .props(ActorName.ACTOR_COMMON_BEAN_NAME), ActorName.ACTOR_COMMON_NAME);
    }

    @Bean(name = ActorName.ACTOR_SUPERVISOR_NAME)
    public ActorRef createsSupervisorActor(){
        ActorSystem actorSystem = actorSystem();
        return actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem)
                .props(ActorName.ACTOR_SUPERVISOR_BEAN_NAME), ActorName.ACTOR_SUPERVISOR_NAME);
    }
}