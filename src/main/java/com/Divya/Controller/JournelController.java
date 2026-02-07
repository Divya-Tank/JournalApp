package com.Divya.Controller;

import com.Divya.Entity.JournalEntry;
import com.Divya.Entity.User;
import com.Divya.Service.JournalService;
import com.Divya.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/Journal")
public class JournelController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;


    @GetMapping
    private ResponseEntity<List<JournalEntry>> getJournalByUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getEntry(userName);
        List<JournalEntry> journal = user.getJournalEntries();
        if(journal!=null){
            return new ResponseEntity<>(journal,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    private ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry) {
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            journalService.saveEntery(entry,userName);
            return new ResponseEntity<>(entry,HttpStatus.CREATED);
        }catch (Exception e){
            log.error("Error occured in adding Journal Entry ",e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/id/{id}")
    private ResponseEntity<JournalEntry> findbyId(@PathVariable ObjectId id) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getEntry(userName);
        List<JournalEntry> journals = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        JournalEntry journalEntry = journals.get(0);
        if (journalEntry!=null && !journalEntry.equals("")){
            Optional<JournalEntry> entry = journalService.getEntry(journalEntry.getId());
            return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{Id}")
    private ResponseEntity<?> updateById(@RequestBody JournalEntry newEntry, @PathVariable ObjectId Id) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getEntry(userName);
        List<JournalEntry> journls = user.getJournalEntries().stream().filter(x->x.getId().equals(Id)).collect(Collectors.toList());
            Optional<JournalEntry> journalEntry = journalService.getEntry(Id);
            if(journalEntry.isPresent()){
                JournalEntry existingEntry = journalEntry.get();
            existingEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : existingEntry.getTitle());
            existingEntry.setName(newEntry.getName() != null && !newEntry.getName().equals("") ? newEntry.getName() : existingEntry.getName());
            journalService.saveEntery(existingEntry);
            return new ResponseEntity<>(existingEntry,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{Id}")
    private ResponseEntity<?> deleteById(@PathVariable ObjectId Id) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getEntry(userName);
        JournalEntry entry = journalService.getEntry(Id).orElse(null);
        if (entry != null) {
            journalService.deleteEntry(user,entry.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
