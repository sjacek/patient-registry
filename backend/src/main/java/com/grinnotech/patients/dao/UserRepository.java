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

import com.grinnotech.patients.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;
import java.util.Set;

/**
 *
 * @author jacek
 */
public interface UserRepository extends MongoRepository<User, String>, QueryDslPredicateExecutor<User> {

    @Query("{ active: true }")
    List<User> findAllActive(Sort sort);

    @Query("{$and: [{ $or: [{lastName: {$regex:?0,$options:'i'}}, {firstName: {$regex:?0,$options:'i'}} ]}, { active: true } ]}")
    List<User> findAllWithFilterActive(String filter, Sort sort);

    @Query("{ $and : [ { id: ?0 }, { active: true } ] }")
    User findOneActive(String id);

    @Query("{ $and : [ { email: ?0 }, { active: true } ] }")
    User findOneByEmailActive(String email);

    @Query("{ $and : [ { passwordResetToken: ?0 }, { enabled: true }, { active: true } ] }")
    User findOneByPasswordResetTokenAndEnabled(String passwordResetToken);

    boolean existsByEmailRegexAndIdNot(String email, String id);

    @Query("{ $and : [ { id: ?0 }, { authorities: ?0 }, { enabled: true }, { active: true } ] }")
    Boolean existsByIdAndAuthoritiesActive(String id, Set<String> authorities);

    @Query("{ $and : " +
            "[ { id: { $ne: ?0 }}, { email: {$regex:?0,$options:'i'} }, { enabled: true }, { active: true } ] }")
    Boolean existsByIdNotAndEmailActive(String userId, String email);

    @Query("{ $and : " +
            "[ { email: {$regex:?0,$options:'i'} }, { enabled: true }, { active: true } ] }")
    boolean existsByEmailActive(String email);
}
