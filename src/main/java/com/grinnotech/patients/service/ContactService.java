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
package com.grinnotech.patients.service;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import com.grinnotech.patients.config.security.MongoUserDetails;
import com.grinnotech.patients.dao.ContactRepository;
import com.grinnotech.patients.model.ContactMethod;
import com.grinnotech.patients.util.ValidationMessages;
import com.grinnotech.patients.util.ValidationMessagesResult;
import com.grinnotech.patients.util.ValidationUtil;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Locale;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import com.grinnotech.patients.dao.authorities.RequireEmployeeAuthority;
import static com.grinnotech.patients.util.QueryUtil.getSpringSort;

/**
 *
 * @author Jacek Sztajnke
 */
@Service
@RequireEmployeeAuthority
public class ContactService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private Validator validator;

    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<ContactMethod> read(ExtDirectStoreReadRequest request) {
        List<ContactMethod> list = contactRepository.findAllActive(getSpringSort(request));
        LOGGER.debug("read size:[" + list.size() + "]");
        return new ExtDirectStoreResult<>(list);
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ExtDirectStoreResult<ContactMethod> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, ContactMethod contact) {
        ExtDirectStoreResult<ContactMethod> result = new ExtDirectStoreResult<>();

        LOGGER.debug("destroy 1");
        ContactMethod old = contactRepository.findOne(contact.getId());

        old.setId(null);
        old.setActive(false);
        contactRepository.save(old);
        LOGGER.debug("destroy 2 " + old.getId());

        setAttrsForDelete(contact, userDetails, old);
        contactRepository.save(contact);
        LOGGER.debug("destroy end");
        return result.setSuccess(true);
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ValidationMessagesResult<ContactMethod> update(@AuthenticationPrincipal MongoUserDetails userDetails, ContactMethod contact) {
        List<ValidationMessages> violations = validateEntity(contact, userDetails.getLocale());

        ValidationMessagesResult<ContactMethod> result = new ValidationMessagesResult<>(contact);
        result.setValidations(violations);

        LOGGER.debug("update 1: " + contact.toString());
        if (violations.isEmpty()) {
            ContactMethod old = contactRepository.findOne(contact.getId());
            if (old != null) {
                old.setId(null);
                old.setActive(false);
                contactRepository.save(old);
                LOGGER.debug("update 2 " + old.getId());
                setAttrsForUpdate(contact, userDetails, old);
            }
            else {
                setAttrsForCreate(contact, userDetails);
            }

            contactRepository.save(contact);
            LOGGER.debug("update 3");
        }

        LOGGER.debug("update end");
        return result;
    }

    private List<ValidationMessages> validateEntity(ContactMethod contact, Locale locale) {
        List<ValidationMessages> validations = ValidationUtil.validateEntity(validator, contact);

        // TODO:
        return validations;
    }
}
