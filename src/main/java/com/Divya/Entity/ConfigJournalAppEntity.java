package com.Divya.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigJournalAppEntity {
    private String key;
    private String value;
}
