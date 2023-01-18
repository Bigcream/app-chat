package com.example.appchat.model.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;

@Document(collection = "role")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity {

    @Id
    private String id;
    private String name;
}
