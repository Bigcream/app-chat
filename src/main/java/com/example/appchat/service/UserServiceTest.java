//package com.example.appchat.service;
//
//import com.example.appchat.model.entity.UserEntity;
//import com.example.appchat.repository.IUserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserServiceTest {
//
//    @Autowired
//    private IUserRepository userRepo;
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    public UserServiceTest(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public UserEntity getAllUser(){
//        // Set gia trị có key loda_list
//        redisTemplate.opsForValue().set("test", userRepo.findByUsername("bigcream"));
//
//        UserEntity user = (UserEntity) redisTemplate.opsForValue().get("test");
//        return user;
//    }
//}
