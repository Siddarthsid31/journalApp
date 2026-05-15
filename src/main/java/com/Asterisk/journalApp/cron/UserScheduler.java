package com.Asterisk.journalApp.cron;

import com.Asterisk.journalApp.entity.JournalEntry;
import com.Asterisk.journalApp.entity.User;
import com.Asterisk.journalApp.enums.Sentiment;
import com.Asterisk.journalApp.model.SentimentData;
import com.Asterisk.journalApp.repository.UserRepository;
import com.Asterisk.journalApp.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserScheduler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSaMail() {
        log.info("Scheduler triggered at: {}", LocalDateTime.now());
        List<User> users = userRepository.getUserForSA();
        log.info("Found {} users to process", users.size());
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();

            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(x -> x.getDate() != null &&
                            x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(JournalEntry::getSentiment)
                    .collect(Collectors.toList());

            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                if (sentiment != null) {
                    sentimentCounts.put(sentiment,
                            sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }

            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            if (mostFrequentSentiment != null) {
                SentimentData sentimentData = SentimentData.builder()
                        .email(user.getEmail())
                        .sentiment("Sentiment for last 7 days: " + mostFrequentSentiment)
                        .build();
                try{
                    kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
                }catch (Exception e){
                    emailService.sendEmail(sentimentData.getEmail(),"Sentiment for previous week", sentimentData.getSentiment());
                }

            }
        }
    }
}