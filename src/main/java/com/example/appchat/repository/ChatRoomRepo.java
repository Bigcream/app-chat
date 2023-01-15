package com.example.appchat.repository;


import com.example.appchat.model.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepo extends JpaRepository<ChatRoomEntity, Long> {
}
