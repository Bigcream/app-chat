package com.example.appchat.kafka.consumer;

import com.example.appchat.constant.KafkaGroup;
import com.example.appchat.constant.KafkaTopic;
import com.kafkaservice.payload.MessageKafka;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationConsumer.class);

    @KafkaListener(topics = KafkaTopic.CREATE_CONVERSATION, groupId = KafkaGroup.CREATE_CONVERSATION_GROUP)
    public void createConversation1(ConsumerRecord<String, String> record, MessageKafka messageKafka) throws Exception {
        sendMessagePublic(record, messageKafka);
        LOGGER.info("Message received create 1");
    }

    public void sendMessagePublic(ConsumerRecord<String, String> record, MessageKafka messageKafka) throws Exception {
        System.out.println("test" + messageKafka.getContent());
    }
}

