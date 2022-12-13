package com.example.appchat.config;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.example.appchat.actor.common.SpringExtension.SPRING_EXTENSION_PROVIDER;


@Configuration
@AllArgsConstructor
public class ActorConfig {
    
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public Config akkaConfig(){
        return ConfigFactory.load();
    }

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("akka-spring-demo", akkaConfig());
        SPRING_EXTENSION_PROVIDER.get(system)
                .initialize(applicationContext);
        return system;
    }
    @Bean(name = "actorCommon")
    public ActorRef create(){
        ActorSystem actorSystem = actorSystem();
        ActorRef greeter = actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem)
                .props("commonActor"), "actorCommon");
        return greeter;
    }

    @Bean(name = "userActor1")
    public ActorRef create1(){
        ActorSystem actorSystem = actorSystem();
        ActorRef testActor = actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem)
                .props("userActor"), "userActor");
        return testActor;
    }
}