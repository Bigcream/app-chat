package com.example.appchat.kafka.consumer;

import com.example.appchat.model.dto.MessageKafka;
import com.example.appchat.model.entity.UserEntity;
import com.example.appchat.service.UserChatService;
import com.example.appchat.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivateChatConsumer {
    private final UserChatService userChatService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicChatConsumer.class);
    private final ObjectMapperUtil objectMapperUtil;


    @KafkaListener(topics = "privateTopicChat", groupId = "groupPrivateChat")
    public void privateChatConsumer1(ConsumerRecord<String, String> record, MessageKafka messageKafka) throws Exception {
        sendMessagePrivate(record, messageKafka);
        LOGGER.info("Message received 1");
    }

    @KafkaListener(topics = "privateTopicChat", groupId = "groupPrivateChat")
    public void privateChatConsumer2(ConsumerRecord<String, String> record, MessageKafka messageKafka) throws Exception {
        sendMessagePrivate(record, messageKafka);
        LOGGER.info("Message received 2");
    }

    @KafkaListener(topics = "privateTopicChat", groupId = "groupPrivateChat")
    public void privateChatConsumer3(ConsumerRecord<String, String> record, MessageKafka messageKafka) throws Exception {
        sendMessagePrivate(record, messageKafka);
        LOGGER.info("Message received 3");
    }

    public void sendMessagePrivate(ConsumerRecord<String, String> record, MessageKafka messageKafka) throws Exception {
        UserEntity user = objectMapperUtil.convertObject(UserEntity.class, messageKafka.getData());
        messageKafka.setSenderName(user.getUsername());
        messageKafka.setReceiverName(user.getUsername());
        userChatService.sendPrivateChat(messageKafka);
        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
    }
}
