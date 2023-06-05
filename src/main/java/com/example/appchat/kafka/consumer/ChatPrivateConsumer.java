package com.example.appchat.kafka.consumer;


import com.example.appchat.model.dto.MessageKafka;
import com.example.appchat.service.UserChatService;
import com.example.appchat.util.SerializerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.example.appchat.constant.KafkaGroup.PRIVATE_CHAT_GROUP;
import static com.example.appchat.constant.KafkaTopic.PRIVATE_CHAT_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatPrivateConsumer {
    private final UserChatService userChatService;

    @KafkaListener(topics = PRIVATE_CHAT_TOPIC, groupId = PRIVATE_CHAT_GROUP)
    public void bankAccountMongoProjectionListener(@Payload byte[] data, ConsumerRecord<String, String> record, ConsumerRecordMetadata meta, Acknowledgment ack) {
        log.info("(privateTopicChat) topic: {}, offset: {}, partition: {}, timestamp: {}, data: {}", meta.topic(), meta.offset(), meta.partition(), meta.timestamp(), new String(data));
        try {
            MessageKafka messageKafka = SerializerUtils.deserializeFromJsonBytes(data, MessageKafka.class);
            sendPrivateChat(record, messageKafka);
            ack.acknowledge();
            log.info("ack events: {}", messageKafka);
        } catch (Exception ex) {
            ack.nack(Duration.ofMillis(100));
            log.error("(privateTopicChat) topic: {}, offset: {}, partition: {}, timestamp: {}", meta.topic(), meta.offset(), meta.partition(), meta.timestamp(), ex);
        }
    }
    public void sendPrivateChat(ConsumerRecord<String, String> record, MessageKafka messageKafka) {
        userChatService.sendPrivateChat(messageKafka);
    }
}
