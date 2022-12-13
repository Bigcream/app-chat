package com.example.appchat.actor.chatroom;


import com.example.appchat.model.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
}
