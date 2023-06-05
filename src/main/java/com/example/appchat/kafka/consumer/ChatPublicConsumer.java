package com.example.appchat.kafka.consumer;

import com.example.appchat.constant.KafkaGroup;
import com.example.appchat.constant.KafkaTopic;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatPublicConsumer {
    private final UserChatService userChatService;

    @KafkaListener(topics = KafkaTopic.PUBLIC_CHAT_TOPIC, groupId = KafkaGroup.PUBLIC_CHAT_GROUP)
    public void bankAccountMongoProjectionListener(@Payload byte[] data, ConsumerRecord<String, String> record, ConsumerRecordMetadata meta, Acknowledgment ack) {
        log.info("(publicTopicChat) topic: {}, offset: {}, partition: {}, timestamp: {}, data: {}", meta.topic(), meta.offset(), meta.partition(), meta.timestamp(), new String(data));
        try {
            MessageKafka messageKafka = SerializerUtils.deserializeFromJsonBytes(data, MessageKafka.class);
            sendMessagePublic(record, messageKafka);
            ack.acknowledge();
            log.info("ack events: {}", messageKafka);
        } catch (Exception ex) {
            ack.nack(Duration.ofMillis(100));
            log.error("(publicTopicChat) topic: {}, offset: {}, partition: {}, timestamp: {}", meta.topic(), meta.offset(), meta.partition(), meta.timestamp(), ex);
        }
    }
    public void sendMessagePublic(ConsumerRecord<String, String> record, MessageKafka messageKafka) {
        userChatService.sendPublicChat(messageKafka);
    }
}

