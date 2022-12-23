package com.example.appchat.config;

import com.example.appchat.model.entity.ChatRoomEntity;
import com.example.appchat.model.entity.UserEntity;
import com.example.appchat.repository.IUserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
@RequiredArgsConstructor
public class ConfigCommon {
    private final IUserRepository userRepo;

    @Bean
    public ObjectMapper getObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/v1/**").allowedOrigins("http://localhost:3000");
            }
        };
    }
    @Bean(name = "mapId")
    public HashMap<String, AtomicLong> generateIdUserTable(){
        Optional<UserEntity> user = Optional.ofNullable(userRepo.getUserIdMax());
        Long userId = 1L;
        if(user.isPresent()){
            userId = userRepo.getUserIdMax().getId();
        }
        HashMap<String, AtomicLong> mapId = new HashMap<>();
        mapId.put("userId", new AtomicLong(userId));
        return mapId;
    }

    @Bean(name = "chatRoomMap")
    public HashMap<Long, List<ChatRoomEntity>> getChatRoomMap(){
        return new HashMap<>();
    }

}
