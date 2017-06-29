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

import com.grinnotech.patients.model.Authority;
import com.grinnotech.patients.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Set;

/**
 *
 * @author jacek
 */
public interface UserRepository extends MongoRepository<User, String> /*, QueryDslPredicateExecutor<User> */ {

    @Query("{ deleted: false }")
    List<User> findAllNotDeleted();

    @Query("{ active: true }")
    List<User> findAllActive(Sort sort);

    @Query("{$and: [{ $or:["
            + " {lastName: {$regex:?0,$options:'i'}}, {firstName: {$regex:?0,$options:'i'}}, {pesel: {$regex:?0,$options:'i'}},"
            + " { active: true } ]}")
    List<User> findAllWithFilterActive(String filter, Sort sort);

    @Query("{ $and : [ { id: ?0 }, { deleted: false } ] }")
    User findOneNotDeleted(String id);

    @Query("{ $and : [ { id: ?0 }, { active: true } ] }")
    User findOneActive(String id);

    @Query("{ $and : [ { email: ?0 }, { deleted: false } ] }")
    User findOneByEmailNotDeleted(String email);

    @Query("{ $and : [ { email: ?0 }, { active: true } ] }")
    User findByEmailActive(String email);

    @Query("{ $and : [ { passwordResetToken: ?0 }, { deleted: false }, { enabled: true } ] }")
    User findByPasswordResetTokenNotDeletedAndEnabled(String passwordResetToken);

    @Query("{ $and : [ { passwordResetToken: ?0 }, { active: true }, { enabled: true } ] }")
    User findByPasswordResetTokenActiveAndEnabled(String passwordResetToken);

//    Long countByEmailRegexAndIdNot(String email, String id);
    boolean existsByEmailRegexAndIdNot(String email, String id);

    @Query("{ $and : [ { id: ?0 }, { authorities: ?0 }, { deleted: false }, { enabled: true } ] }")
    long countByIdAndAuthoritiesActive(String id, Set<String> authorities);

    @Query("{ $and : " +
            "[ { id: { $ne: ?0 }}, { email: {$regex:?0,$options:'i'} }, { deleted: false }, { enabled: true } ] }")
    long countByIdNotAndEmailActive(String userId, String email);

    @Query("{ $and : " +
            "[ { email: {$regex:?0,$options:'i'} }, { deleted: false }, { enabled: true } ] }")
    long countByEmailActive(String email);
}
