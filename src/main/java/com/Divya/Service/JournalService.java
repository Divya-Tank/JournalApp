package com.Divya.Service;

import com.Divya.Entity.JournalEntry;
import com.Divya.Entity.User;
import com.Divya.Repository.JournalRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Service
@Slf4j
public class JournalService  {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntery(JournalEntry journalEntry, String userName){
        try{
        User user = userService.getEntry(userName);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalRepository.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.saveUser(user);
        }catch(Exception e){
            log.error("Exception occured in saving user {}" , userName , e);
        }

    }

    public void saveEntery(JournalEntry journalEntry){
        try{
            journalEntry.setDate(LocalDateTime.now());
            journalRepository.save(journalEntry);
        }catch(Exception e){
            log.error("Exception occured in saving Journal Entry : {}",journalEntry.getName(),e);
        }

    }

    public Optional<JournalEntry> getEntry(ObjectId id){
        return journalRepository.findById(id);
    }

    public List<JournalEntry> getAll(){
       return journalRepository.findAll();
    }

    @Transactional
    public boolean deleteEntry(User user,ObjectId id){
        boolean removed=false;
        try {
            removed = userService.getEntry(user.getUserName()).getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed){
                journalRepository.deleteById(id);
                userService.saveUser(user);
            }
        }catch (Exception e){
            log.error("JournalEntry {} not deleted!!" , e);
        }
        return removed;

    }



}
