package com.example.appchat.kafka.consumer;

import com.example.appchat.constant.KafkaGroup;
import com.example.appchat.constant.KafkaTopic;
import com.example.appchat.service.UserChatService;
import com.kafkaservice.payload.MessageKafka;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatPublicConsumer {
    private final UserChatService userChatService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatPublicConsumer.class);

    @KafkaListener(topics = KafkaTopic.PUBLIC_CHAT_TOPIC, groupId = KafkaGroup.PUBLIC_CHAT_GROUP)
    public void publicChatConsumer1(ConsumerRecord<String, String> record, MessageKafka messageKafka) {
        sendMessagePublic(record, messageKafka);
        LOGGER.info("Message received 1");
    }

    @KafkaListener(topics = KafkaTopic.PUBLIC_CHAT_TOPIC, groupId = KafkaGroup.PUBLIC_CHAT_GROUP)
    public void publicChatConsumer2(ConsumerRecord<String, String> record, MessageKafka messageKafka) {
        sendMessagePublic(record, messageKafka);
        LOGGER.info("Message received 2");
    }

    @KafkaListener(topics = KafkaTopic.PUBLIC_CHAT_TOPIC, groupId = KafkaGroup.PUBLIC_CHAT_GROUP)
    public void publicChatConsumer3(ConsumerRecord<String, String> record, MessageKafka messageKafka) {
        sendMessagePublic(record, messageKafka);
        LOGGER.info("Message received 3");
    }

    public void sendMessagePublic(ConsumerRecord<String, String> record, MessageKafka messageKafka) {
        userChatService.sendPublicChat(messageKafka);
        System.out.println("test" + messageKafka.getContent());
    }
}

