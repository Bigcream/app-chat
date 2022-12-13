package com.example.appchat.config;

import com.example.appchat.model.entity.ChatRoomEntity;
import com.example.appchat.model.entity.UserEntity;
import com.example.appchat.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
@RequiredArgsConstructor
public class ConfigCommon {
    private final IUserRepository userRepo;
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
