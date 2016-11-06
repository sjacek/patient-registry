package com.grinno.patients.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.grinno.patients.model.Patient;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 *
 * @author jacek
 */
class PatientRepositoryImpl implements PatientRepositoryCustom {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String COLLECTION_NAME = "patient";

    @Autowired
    private MongoTemplate mongoTemplate;

//    /**
//     *
//     * @param filter
//     * @param skip
//     * @param limit
//     * @return
//     */
//    @Override
//    public List<Patient> findAllWithFilter(String filter, int skip, int limit) {
//        Query query = new Query();
//        query.skip(skip);
//        query.limit(limit);
//        query.addCriteria(new Criteria().orOperator(
//                Criteria.where("lastName").regex(filter),
//                Criteria.where("firstName").regex(filter),
//                Criteria.where("pesel").regex(filter)));
//
//        return mongoTemplate.find(query, Patient.class, COLLECTION_NAME);
//    }

}
