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
package com.grinno.patients.service;

import com.grinno.patients.config.security.MongoUserDetails;
import com.grinno.patients.dao.UserRepository;
import com.grinno.patients.domain.AbstractPersistable;
import com.grinno.patients.model.User;
import java.util.Date;
import org.springframework.context.MessageSource;

/**
 *
 * @author jacek
 */
public abstract class AbstractService {

    UserRepository userRepository;

    MessageSource messageSource;

    AbstractService(UserRepository userRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }
    
    public void setAttrsForUpdate(AbstractPersistable persistable, MongoUserDetails userDetails) {
        User user = slimDown(userDetails.getUser(userRepository));
        
        if (persistable.getCreatedDate() == null) {
            persistable.setCreatedDate(new Date());
        }
        if (persistable.getCreatedBy() == null) {
            persistable.setCreatedBy(user);
        }
        persistable.incrementVersion();
        persistable.setDeleted(false);
    }

    public void setAttrsForDelete(AbstractPersistable persistable, MongoUserDetails userDetails) {
        User user = slimDown(userDetails.getUser(userRepository));

        persistable.setDeleted(true);
        persistable.setDeletedDate(new Date());
        persistable.setDeletedBy(user);
    }

    private User slimDown(User user) {
        User slimUser = new User();
        slimUser.setAuthorities(user.getAuthorities());
        slimUser.setDeleted(user.isDeleted());
        slimUser.setEmail(user.getEmail());
        slimUser.setEnabled(user.isEnabled());
        slimUser.setFirstName(user.getFirstName());
        slimUser.setId(user.getId());
        slimUser.setLastName(user.getLastName());
            
        return slimUser;
    }
}
