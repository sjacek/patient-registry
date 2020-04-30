/*
 * Copyright (C) 2017 Jacek Sztajnke
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
package com.grinnotech.patients.dao.orphadata;

import com.grinnotech.patients.model.orphadata.Disorder;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
//import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;

/**
 * @author Jacek Sztajnke
 */
public interface DisorderRepository extends MongoRepository<Disorder, String>
//		, QuerydslPredicateExecutor<Disorder>
{
	@Query("{ $or:["
			+ " {name: {$regex:?0,$options:'i'}}, {icd10: {$regex:?0,$options:'i'}}, {synonyms: {$regex:?0,$options:'i'}} ]} }")
	Collection<Disorder> findAllWithFilter(String filter, Sort sort);
}
