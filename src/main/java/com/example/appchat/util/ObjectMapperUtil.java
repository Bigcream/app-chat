package com.example.appchat.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObjectMapperUtil {
    private final ObjectMapper objectMapper;

    public <T> T convertObject(Class<T> object, Object data){
        return objectMapper.convertValue(data, object);
    }
}
