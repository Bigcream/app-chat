//package com.example.appchat.controller;
//
//import com.example.learn.application.UserChatService;
//import com.example.learn.application.UserServiceTest;
//import com.example.learn.infrastructure.database.dto.UserDTO;
//import com.example.learn.infrastructure.database.entity.UserEntity;
//import com.example.learn.infrastructure.security.CustomUserDetails;
//import com.example.learn.infrastructure.security.jwt.JwtTokenProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1")
//@RequiredArgsConstructor
//public class UserController extends BaseController {
//    private final UserServiceTest userService;
//    private final UserChatService userChatService;
//    private final AuthenticationManager authenticationManager;
//    private final JwtTokenProvider tokenProvider;
//    @GetMapping(path = "/publish", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<UserEntity> publish(@RequestParam("message") String message){
//
//        UserEntity userEntities = userService.getAllUser();
//        return ResponseEntity.ok(userEntities);
//    }
//    @PostMapping("/login")
//    public ResponseEntity<String> authenticateUser(@RequestBody UserEntity user) {
////         Xác thực từ username và password.
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        user.getUsername(),
//                        user.getPassword()
//                )
//        );
//        // Nếu không xảy ra exception tức là thông tin hợp lệ
//        // Set thông tin authentication vào Security Context
//        SecurityContextHolder.getContext().setAuthentication(authentication);
////         Trả về jwt cho người dùng.
//        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
////        UserDTO userDTO = UserDTO.builder().username(user.getUsername()).lastName(user.getUsername()).firstName(user.getUsername()).id(1L).email("bigcream@gmail.com").build();
////        userDTO.setPassWord("123456");
//        return ResponseEntity.ok(jwt);
//    }
//    @PostMapping("/register")
//    public ResponseEntity<UserEntity> register(@RequestBody UserEntity user) throws Exception {
//        return new  ResponseEntity<>(userChatService.register(user), noCacheHeader, HttpStatus.OK);
//    }
//    // Api /api/random yêu cầu phải xác thực mới có thể request
//    @GetMapping("/random")
//    public ResponseEntity<String> randomStuff(){
//        return ResponseEntity.ok("test.get()");
//    }
//
//    @GetMapping("/auth/status")
//    public ResponseEntity<UserDTO> getAuth(){
//        UserDTO userDTO = UserDTO.builder().username("bigcream").lastName("big").firstName("cream").id(1L).email("bigcream@gmail.com").build();
//        return ResponseEntity.ok(userDTO);
//    }
//}
