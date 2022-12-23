package com.example.appchat.model.dto;

import lombok.*;

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
    private String date;
    private Status status;
    private Long roomId;
}
