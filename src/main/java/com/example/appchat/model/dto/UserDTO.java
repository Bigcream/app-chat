package com.example.appchat.model.dto;

import lombok.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserDTO {
    private String sessionId;
    private String username;
    private List<String> destination;
}
