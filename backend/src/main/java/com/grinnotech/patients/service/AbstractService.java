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
import com.grinnotech.patients.util.ValidationMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

/**
 *
 * @author jacek
 */
public abstract class AbstractService<T> {

    @Autowired
    private UserRepository userRepository;

    public void setAttrsForCreate(AbstractPersistable persistable, UserDetails userDetails) {
        User user = slimDown(userRepository.findOneByEmailActive(userDetails.getUsername()));
        
        persistable.setCreatedDate(new Date());
        persistable.setCreatedBy(user);

        persistable.setChainId(persistable.getId());
        persistable.setVersion(1);
        persistable.setActive(true);
    }

    public void setAttrsForUpdate(AbstractPersistable persistable, UserDetails userDetails, AbstractPersistable old) {
        User user = slimDown(userRepository.findOneByEmailActive(userDetails.getUsername()));
        
        persistable.setUpdatedDate(new Date());
        persistable.setUpdatedBy(user);
        persistable.setPrevId(old.getId());
        persistable.setChainId(old.getChainId());
        persistable.setVersion(old.getVersion() + 1);
        persistable.setActive(true);
    }

    public void setAttrsForDelete(AbstractPersistable persistable, UserDetails userDetails, AbstractPersistable old) {
        User user = slimDown(userRepository.findOneByEmailActive(userDetails.getUsername()));

        persistable.setDeletedDate(new Date());
        persistable.setDeletedBy(user);
        persistable.setPrevId(old.getId());
        persistable.setChainId(old.getChainId());
        persistable.setVersion(old.getVersion() + 1);
        persistable.setActive(false);
    }

    private User slimDown(User user) {
        User slimUser = User.builder()
                .authorities(user.getAuthorities())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        slimUser.setId(user.getId());
        slimUser.setActive(user.isActive());

        return slimUser;
    }

    @Autowired
    private Validator validator;

    protected List<ValidationMessages> validateEntity(T entity, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity, groups);
        Map<String, List<String>> fieldMessages = new HashMap<>();
        if (!constraintViolations.isEmpty()) {
            constraintViolations.forEach(constraintViolation -> {
                String property = constraintViolation.getPropertyPath().toString();
                List<String> messages = fieldMessages.computeIfAbsent(property, k -> new ArrayList<>());
                messages.add(constraintViolation.getMessage());
            });
        }
        List<ValidationMessages> validationErrors = new ArrayList<>();
        fieldMessages.forEach((k, v) -> {
            ValidationMessages errors = new ValidationMessages();
            errors.setField(k);
            errors.setMessages(v.toArray(new String[v.size()]));
            validationErrors.add(errors);
        });

        return validationErrors;
    }
}
