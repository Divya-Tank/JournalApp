package com.Divya.Service;

import com.Divya.Enum.Sentiment;
import com.Divya.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServcie {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "Weather-Api",groupId = "weekly-sentiment-group")
    public void consume(SentimentData sentimentData){
        sendMail(sentimentData);
    }

    public void sendMail(SentimentData sentimentData){
        emailService.mailSend(sentimentData.getEmail(),"Sentiment of previous week",sentimentData.getSentiment());
    }
}
