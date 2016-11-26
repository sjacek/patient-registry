/*
 * Copyright (C) 2016 Pivotal Software, Inc.
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
package com.grinno.patients.dao;

import com.grinno.patients.model.ZipCodePoland;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author Jacek Sztajnke
 */
public interface ZipCodePolandRepository extends
        MongoRepository<ZipCodePoland, String>,
        PagingAndSortingRepository<ZipCodePoland, String>,
        QueryDslPredicateExecutor<ZipCodePoland> {

    @Query("{active:true}")
    Page<ZipCodePoland> findAllActive(Pageable pageable);

    @Query("{$and: [{ $or:["
            + " {zipCode: {$regex:?0,$options:'i'}}, {city: {$regex:?0,$options:'i'}}, {voivodship: {$regex:?0,$options:'i'}}, {county: {$regex:?0,$options:'i'}} ]},"
            + " {active:true} ]}")
    Page<ZipCodePoland> findAllWithFilterActive(String filter, Pageable pageable);

    @Query(count = true, value = "{$and: [ {zipCode:?0}, {postOffice:?1}, {city:?2}, {voivodship:?3}, {street:?4}, {county:?5} ]}")
    int CountByExample(String zipCode, String postOffice, String city, String voivodship, String street, String county);
}
