/*
 * Copyright (C) 2016 Jacek Sztajnke
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.grinnotech.patients.dao;

import com.grinnotech.patients.model.Patient;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 *
 * @author jacek
 */
public interface PatientRepository extends
        MongoRepository<Patient, String>,
        QueryDslPredicateExecutor<Patient>,
        PatientRepositoryCustom {

    @Query("{active:true}")
    List<Patient> findAllActive();

    @Query("{active:true}")
    List<Patient> findAllActive(Sort sort);

    @Query("{$and: [{ $or:["
            + " {lastName: {$regex:?0,$options:'i'}}, {firstName: {$regex:?0,$options:'i'}}, {pesel: {$regex:?0,$options:'i'}}," 
            + " {address.city: {$regex:?0,$options:'i'}}, {address.voivodship: {$regex:?0,$options:'i'}}, {address.county: {$regex:?0,$options:'i'}},"
            + " {address.country: {$regex:?0,$options:'i'}} ]},"
            + " {active:true} ]}")
    List<Patient> findAllWithFilterActive(String filter, Sort sort);
}
