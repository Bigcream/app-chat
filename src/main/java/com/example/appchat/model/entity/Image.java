package com.example.appchat.model.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Image {
    private Integer id;
    private String title;
    private String url;
}
