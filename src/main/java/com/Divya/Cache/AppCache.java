package com.Divya.Cache;

import com.Divya.Entity.ConfigJournalAppEntity;
import com.Divya.Repository.ConfigJournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum Keys{
        WEATHER_API
    }

    public Map<String,String> appCache;
    
    @Autowired
    private ConfigJournalRepository configJournalRepository;

   @PostConstruct
    public void init(){
       appCache = new HashMap<>();
       List<ConfigJournalAppEntity> entries = configJournalRepository.findAll();
       for(ConfigJournalAppEntity entry : entries){
           appCache.put(entry.getKey(), entry.getValue());
       }

   }
}
