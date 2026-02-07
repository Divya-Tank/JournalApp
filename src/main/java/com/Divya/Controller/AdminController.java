package com.Divya.Controller;


import com.Divya.Entity.JournalEntry;
import com.Divya.Entity.User;
import com.Divya.Enum.Sentiment;
import com.Divya.Service.EmailService;
import com.Divya.Service.JournalService;
import com.Divya.Service.UserService;
import com.Divya.model.SentimentData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JournalService journalService;

    @Autowired
    private KafkaTemplate<String,SentimentData> kafkaTemplate;

    @GetMapping("/all-users")
    private ResponseEntity<List<User>> getall() {
        List<User> all = userService.getAll();
        if(all!=null){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create-admin-user")
    private void CreateAdminUser(@RequestBody User user){
        userService.saveNewAdminUser(user);
            }

    @GetMapping("/userForSA")
    private List<User> getUserforSA(){
        List<User> users = userService.userforSA();
        return users;
    }

    @PostMapping("/sendMail")
    private void sendMail(){
        List<User> users = userService.userforSA();
        for (User user: users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x->x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x->x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment,Integer> SentimentCount = new HashMap<>();
            for(Sentiment sentiment:sentiments){
                if(sentiment!=null){
                    SentimentCount.put(sentiment,SentimentCount.getOrDefault(sentiment,0)+1);
                }
            }
            Sentiment mostFrequestSentiment = null;
            int MaxCount=0;
            for(Map.Entry<Sentiment,Integer> entry:SentimentCount.entrySet()){
                if(entry.getValue()>MaxCount){
                    MaxCount= entry.getValue();
                    mostFrequestSentiment = entry.getKey();
                }
            }
            if(mostFrequestSentiment!=null){
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Hello "+ user.getUserName() + " Your sentiment Analysis is " +mostFrequestSentiment).build();
                try {
                    kafkaTemplate.send("Weather-Api",sentimentData.getEmail(),sentimentData);
                }catch (Exception e){
                    log.error("Kafka error" + e);
                    emailService.mailSend(user.getEmail(),"Sentiment Analysis","Hello "+ user.getUserName() + " Your sentiment Analysis is " +mostFrequestSentiment);
                }


            }

        }
    }
}
