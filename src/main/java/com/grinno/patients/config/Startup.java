package com.grinno.patients.config;

import com.grinno.patients.dao.DictionaryRepository;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mongodb.client.MongoCollection;

import com.grinno.patients.model.Authority;
import com.grinno.patients.model.User;

@Component
class Startup {

    private final MongoDb mongoDb;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final DictionaryRepository dictionaryRepository;
    
    @Autowired
    public Startup(MongoDb mongoDb, DictionaryRepository dictionaryRepository, PasswordEncoder passwordEncoder) {
        this.mongoDb = mongoDb;
        this.dictionaryRepository = dictionaryRepository;
        this.passwordEncoder = passwordEncoder;
        init();
    }

    private void init() {

        MongoCollection<User> userCollection = mongoDb.getCollection(User.class);
        if (userCollection.count() == 0) {
            // admin user
            User adminUser = new User();
            adminUser.setEmail("admin@starter.com");
            adminUser.setFirstName("admin");
            adminUser.setLastName("admin");
            adminUser.setLocale("en");
            adminUser.setPasswordHash(this.passwordEncoder.encode("admin"));
            adminUser.setEnabled(true);
            adminUser.setDeleted(false);
            adminUser.setAuthorities(Arrays.asList(Authority.ADMIN.name()));
            userCollection.insertOne(adminUser);

            // normal user
            User normalUser = new User();
            normalUser.setEmail("user@starter.com");
            normalUser.setFirstName("user");
            normalUser.setLastName("user");
            normalUser.setLocale("de");
            normalUser.setPasswordHash(this.passwordEncoder.encode("user"));
            normalUser.setEnabled(true);
            adminUser.setDeleted(false);
            normalUser.setAuthorities(Arrays.asList(Authority.USER.name()));
            userCollection.insertOne(normalUser);
        }

//        MongoCollection<Patient> patientCollection = mongoDb.getCollection(Patient.class);
//        if (patientCollection.count() == 0) {
//        }
//
//        Dictionary dictionary = new Dictionary();
//        dictionary.setName("blabla");
//        dictionaryRepository.insert(dictionary);
    }

}
