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
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author jacek
 */
public interface UserRepository extends MongoRepository<User, String> /*, QueryDslPredicateExecutor<User> */ {

    @Query("{ $and : [ { id: ?0 }, { deleted: false } ] }")    
    User findOneNotDeleted(String id);

    @Query("{ $and : [ { email: ?0 }, { deleted: false } ] }")    
    User findByEmailNotDeleted(String email);

    @Query("{ $and : [ { passwordResetToken: ?0 }, { deleted: false }, { enabled: true } ] }")    
    User findByPasswordResetTokenNotDeletedAndEnabled(String passwordResetToken);
    
    Long countByEmailRegexAndIdNot(String email, String id);
}
