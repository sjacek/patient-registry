package com.grinnotech.patients.domain;

import org.bson.codecs.IdGenerator;

import java.util.UUID;

public class UUIDStringGenerator implements IdGenerator {

    @Override
    public Object generate() {
        return UUID.randomUUID().toString();
    }

}
