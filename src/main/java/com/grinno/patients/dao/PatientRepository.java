package com.grinno.patients.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.grinno.patients.model.Patient;
import com.mongodb.WriteResult;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class PatientRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String COLLECTION_NAME = "patient";

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *
     * @param id
     * @return
     */
    public Patient findOneById(String id) {
        return mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Patient.class, COLLECTION_NAME);
    }

    /**
     *
     * @param pesel
     * @return
     */
    public Patient findOneByPesel(String pesel) {
        return mongoTemplate.findOne(Query.query(Criteria.where("pesel").is(pesel)), Patient.class, COLLECTION_NAME);
    }

    /**
     *
     * @return
     */
    public List<Patient> findAll() {
        return mongoTemplate.findAll(Patient.class, COLLECTION_NAME);
    }

    /**
     *
     * @param filter
     * @param skip
     * @param limit
     * @return
     */
    public List<Patient> findAllWithFilter(String filter, int skip, int limit) {
        LOGGER.debug("findAllWithFilter mongodb:" + mongoTemplate.getDb());
        Query query = new Query();
        query.skip(skip);
        query.limit(limit);
        query.addCriteria(Criteria.where("lastName").regex(filter));
        query.addCriteria(Criteria.where("firstName").regex(filter));
        query.addCriteria(Criteria.where("pesel").regex(filter));
        
        return mongoTemplate.find(query, Patient.class, COLLECTION_NAME);
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

        WriteResult result = mongoTemplate.updateFirst(query, update, Patient.class);
        if (result.getN() == 0) {
            mongoTemplate.save(patient, COLLECTION_NAME);
        }

        return patient;
    }

}
