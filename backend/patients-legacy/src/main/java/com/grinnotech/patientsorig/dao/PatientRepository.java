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
package com.grinnotech.patientsorig.dao;

import com.grinnotech.patientsorig.model.Patient;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
//import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

/**
 * @author Jacek Sztajnke
 */
public interface PatientRepository extends
		MongoRepository<Patient, String>
//		, QuerydslPredicateExecutor<Patient>
{

	@Query("{$and: [ {organizationId: ?0}, {active:true} ]}")
	List<Patient> findByOrganizationIdActive(String organizationId, Sort sort);

	@Query("{$and: [ {organizationId: ?0}, " + " {$or:["
			+ "   {lastName: {$regex:?1,$options:'i'}}, {firstName: {$regex:?1,$options:'i'}}, {pesel: {$regex:?1,$options:'i'}},"
			+ "   {address.city: {$regex:?1,$options:'i'}}, {address.voivodship: {$regex:?1,$options:'i'}},"
			+ "   {address.county: {$regex:?1,$options:'i'}}, {address.country: {$regex:?1,$options:'i'}} ]},"
			+ " {active:true} ]}")
	List<Patient> findByOrganizationIdWithFilterActive(String organizationId, String filter, Sort sort);
}
