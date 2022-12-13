package com.example.appchat.model.dto;


import com.example.appchat.model.entity.UserEntity;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ChatRoomDTO {
    private Long chatRoomId;
    private UserEntity createBy;
    private Set<UserEntity> userInRooms;
}
