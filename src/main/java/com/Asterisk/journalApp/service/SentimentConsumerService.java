package com.Asterisk.journalApp.service;

import com.Asterisk.journalApp.model.SentimentData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "weekly-sentiments", groupId = "weekly-sentiment-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(byte[] data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SentimentData sentimentData = mapper.readValue(data, SentimentData.class);
            sendEmail(sentimentData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(SentimentData sentimentData) {
        emailService.sendEmail(sentimentData.getEmail(),
                "Sentiment for previous week",
                sentimentData.getSentiment());
    }
}