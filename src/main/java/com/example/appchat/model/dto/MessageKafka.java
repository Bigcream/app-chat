package com.example.appchat.model.dto;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MessageKafka {
    private Object data;
    private String sender;
    private String receiver;
    private String content;
    private Timestamp time;
    private Status status;
    private String conversationId;
    private Long senderId;
    private Long receiverId;
}
