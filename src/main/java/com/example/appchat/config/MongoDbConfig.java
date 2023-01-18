package com.example.appchat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
@RequiredArgsConstructor
public class MongoDbConfig {

    private final MappingMongoConverter mappingMongoConverter;

    @Bean
    public void afterPropertiesSet(){
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}
