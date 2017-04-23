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

import com.grinnotech.patients.model.Organization;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 *
 * @author jacek
 */
public interface OrganizationRepository extends
        MongoRepository<Organization, String>,
        QueryDslPredicateExecutor<Organization> {

    @Query("{active:true}")
    List<Organization> findAllActive(Sort sort);

    @Query("{$and: [{ $or:["
            + " {name: {$regex:?0,$options:'i'}}, {code: {$regex:?0,$options:'i'}} ]},"
            + " {active:true} ]}")
    List<Organization> findAllWithFilterActive(String filter, Sort sort);

    @Query("{ $and : [ { id: ?0 }, { active: true } ] }")
    Organization findOneActive(String id);

    @Query("{ $and : [ { code: ?0 }, { active: true } ] }")
    Organization findByCodeActive(String code);
}
