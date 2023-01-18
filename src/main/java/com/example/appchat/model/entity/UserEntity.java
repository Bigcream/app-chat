package com.example.appchat.model.entity;

import com.example.appchat.model.dto.UserDTO;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "user")
public class UserEntity {

    @Id
    protected String id;
    private String username;
    private String email;
    private String password;
    private Set<RoleEntity> roles;

    public UserDTO convertToDTO(){
        return UserDTO.builder()
                .email(this.getEmail())
                .id(this.getId())
                .build();
    }

}