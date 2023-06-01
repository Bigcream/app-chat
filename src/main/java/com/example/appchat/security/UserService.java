//package com.example.appchat.security;
//
//import com.example.appchat.model.entity.UserEntity;
//import com.example.appchat.repository.UserRepo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class UserService implements UserDetailsService {
//    private final UserRepo userRepo;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<UserEntity> userEntity = userRepo.findByUsername(username);
//        if(!userEntity.isPresent()){
//            throw new UsernameNotFoundException("User not found");
//        }
//        return new CustomUserDetails(userEntity.get());
//    }
//}
