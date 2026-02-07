package com.Divya.Repository;

import com.Divya.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUserforSA(){
        Query  query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.andOperator(Criteria.where("email").regex("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,6}$"),
                Criteria.where("sentimentAnalysis").exists(true)));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }


}
