package com.example.appchat.model.dto;


import com.kafkaservice.payload.MessageKafka;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ChatMessage {
    private MessageKafka message;
}
