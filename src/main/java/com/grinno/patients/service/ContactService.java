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

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import com.grinno.patients.config.security.MongoUserDetails;
import com.grinno.patients.config.security.RequireEmpolyeeAuthority;
import com.grinno.patients.dao.ContactRepository;
import com.grinno.patients.dao.UserRepository;
import com.grinno.patients.model.Contact;
import static com.grinno.patients.service.PatientService.LOGGER;
import com.grinno.patients.util.ValidationMessages;
import com.grinno.patients.util.ValidationMessagesResult;
import com.grinno.patients.util.ValidationUtil;
import java.util.List;
import java.util.Locale;
import javax.validation.Validator;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jacek Sztajnke
 */
@Service
@RequireEmpolyeeAuthority
public class ContactService extends AbstractService {

    private final ContactRepository contactRepository;
    private final Validator validator;

    private final MessageSource messageSource;
    
    public ContactService(ContactRepository contactRepository, UserRepository userRepository, Validator validator, MessageSource messageSource) {
        super(userRepository, messageSource);
        this.contactRepository = contactRepository;
        this.validator = validator;
        this.messageSource = messageSource;
    }
    
    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<Contact> read(ExtDirectStoreReadRequest request) {
        List<Contact> list = contactRepository.findAllNotDeleted();
        LOGGER.debug("read size:[" + list.size() + "]");
        return new ExtDirectStoreResult<>(list);
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ExtDirectStoreResult<Contact> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, Contact contact) {
        ExtDirectStoreResult<Contact> result = new ExtDirectStoreResult<>();

        LOGGER.debug("destroy 1");
        setAttrsForDelete(contact, userDetails);
        contactRepository.save(contact);
        LOGGER.debug("destroy end");
        return result.setSuccess(true);
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ValidationMessagesResult<Contact> update(@AuthenticationPrincipal MongoUserDetails userDetails, Contact contact) {
        List<ValidationMessages> violations = validateEntity(contact, userDetails.getLocale());

        ValidationMessagesResult<Contact> result = new ValidationMessagesResult<>(contact);
        result.setValidations(violations);

        LOGGER.debug("update 1: " + contact.toString());
        if (violations.isEmpty()) {
            setAttrsForUpdate(contact, userDetails);
            contactRepository.save(contact);
            LOGGER.debug("update 2");
        }

        LOGGER.debug("update end");
        return result;
    }

    private List<ValidationMessages> validateEntity(Contact contact, Locale locale) {
        List<ValidationMessages> validations = ValidationUtil.validateEntity(validator, contact);

        // TODO:
        return validations;
    }
}
