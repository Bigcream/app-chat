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
    private String senderName;
    private String receiverName;
    private String message;
    private Timestamp time;
    private Status status;
    private Long room_public_id;
}
