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
package com.grinnotech.patients.service;

import com.grinnotech.patients.dao.UserRepository;
import com.grinnotech.patients.domain.AbstractPersistable;
import com.grinnotech.patients.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 *
 * @author jacek
 */
public abstract class AbstractService {

    @Autowired
    private UserRepository userRepository;

    public void setAttrsForCreate(AbstractPersistable persistable, UserDetails userDetails) {
        User user = slimDown(userRepository.findByEmailNotDeleted(userDetails.getUsername()));
        
        persistable.setCreatedDate(new Date());
        persistable.setCreatedBy(user);

        persistable.setChainId(persistable.getId());
        persistable.setVersion(1);
        persistable.setActive(true);
    }

    public void setAttrsForUpdate(AbstractPersistable persistable, UserDetails userDetails, AbstractPersistable old) {
        User user = slimDown(userRepository.findByEmailNotDeleted(userDetails.getUsername()));
        
        persistable.setUpdatedDate(new Date());
        persistable.setUpdatedBy(user);
        persistable.setPrevId(old.getId());
        persistable.setChainId(old.getChainId());
        persistable.setVersion(old.getVersion() + 1);
        persistable.setActive(true);
    }

    public void setAttrsForDelete(AbstractPersistable persistable, UserDetails userDetails, AbstractPersistable old) {
        User user = slimDown(userRepository.findByEmailNotDeleted(userDetails.getUsername()));

        persistable.setDeletedDate(new Date());
        persistable.setDeletedBy(user);
        persistable.setPrevId(old.getId());
        persistable.setChainId(old.getChainId());
        persistable.setVersion(old.getVersion() + 1);
        persistable.setActive(false);
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
