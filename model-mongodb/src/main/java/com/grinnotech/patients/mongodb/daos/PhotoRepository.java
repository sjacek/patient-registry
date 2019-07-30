package com.grinnotech.patients.mongodb.daos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.grinnotech.patients.mongodb.models.Photo;

public interface PhotoRepository extends MongoRepository<Photo, String> {

}
