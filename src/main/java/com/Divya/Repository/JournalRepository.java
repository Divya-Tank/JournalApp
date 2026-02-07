package com.Divya.Repository;

import com.Divya.Entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface JournalRepository extends MongoRepository<JournalEntry,ObjectId> {
}
