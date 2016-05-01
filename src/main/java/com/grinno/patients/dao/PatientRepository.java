package com.grinno.patients.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.grinno.patients.model.Patient;

@Repository
public class PatientRepository {

    public static final String COLLECTION_NAME = "patient";

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *
     * @param patient
     * @return 
     */
    public Patient insert(Patient patient) {
        if (!mongoTemplate.collectionExists(Patient.class)) {
            mongoTemplate.createCollection(Patient.class);
        }
        mongoTemplate.insert(patient, COLLECTION_NAME);
        return patient;
    }

    /**
     *
     * @param id
     * @return
     */
    public Patient findOneById(String id) {
        return mongoTemplate.findOne(
                Query.query(Criteria.where("id").is(id)), Patient.class, COLLECTION_NAME);
    }

    /**
     *
     * @param pesel
     * @return
     */
    public Patient findOneByPesel(String pesel) {
        return mongoTemplate.findOne(
                Query.query(Criteria.where("pesel").is(pesel)), Patient.class, COLLECTION_NAME);
    }

    /**
     *
     * @return
     */
    public List<Patient> findAll() {
        return mongoTemplate.findAll(Patient.class, COLLECTION_NAME);
    }

    public Patient remove(String id) {
        Patient patient = mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(id)), Patient.class, COLLECTION_NAME);
        mongoTemplate.remove(patient, COLLECTION_NAME);

        return patient;
    }

    public Patient updateFirst(String id, Patient patient) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        Update update = new Update();
        update.set("firstName", patient.getFirstName());
        update.set("secondName", patient.getSecondName());
        update.set("lastName", patient.getLastName());
        update.set("pesel", patient.getPesel());

        mongoTemplate.updateFirst(query, update, Patient.class);

        return patient;
    }
}
