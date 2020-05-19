package com.grinnotech.patients.mongodb.dao;

import com.grinnotech.patients.mongodb.model.Photo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhotoRepository extends MongoRepository<Photo, String> {

}
