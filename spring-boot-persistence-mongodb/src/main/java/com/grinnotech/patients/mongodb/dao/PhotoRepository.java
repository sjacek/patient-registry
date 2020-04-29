package com.grinnotech.patients.mongodb.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.grinnotech.patients.mongodb.model.Photo;

public interface PhotoRepository extends MongoRepository<Photo, String> {

}
