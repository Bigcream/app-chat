package com.example.appchat.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity  implements Serializable {

    @Id
//    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    protected Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private ChatRoomEntity chatRoom;

}
