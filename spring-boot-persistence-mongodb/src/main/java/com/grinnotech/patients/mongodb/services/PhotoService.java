package com.grinnotech.patients.mongodb.services;

import com.grinnotech.patients.mongodb.dao.PhotoRepository;
import com.grinnotech.patients.mongodb.model.Photo;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PhotoService {

    private final PhotoRepository photoRepo;

	public PhotoService(PhotoRepository photoRepo) {
		this.photoRepo = photoRepo;
	}

	public Photo getPhoto(String id) {
        return photoRepo.findById(id).get();
    }

    public String addPhoto(String title, MultipartFile file) throws IOException {
        Photo photo = new Photo(title);
        photo.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        photo = photoRepo.insert(photo);
        return photo.getId();
    }
}
