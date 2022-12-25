package com.example.appchat.model.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MessageRoom {
    protected Long id;
    private Long seq;
    private String senderName;
    private String message;
    private Timestamp time;
    private Long roomPublicId;
}
