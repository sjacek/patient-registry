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
package com.grinnotech.patientsorig.service;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import static com.grinnotech.patientsorig.util.OptionalEx.ifPresent;
import static com.grinnotech.patientsorig.util.QueryUtil.getSpringSort;
import static com.grinnotech.patientsorig.util.ThrowingFunction.sneakyThrow;

import com.grinnotech.patientsorig.NotFoundException;
import com.grinnotech.patientsorig.NotFoundExceptionSuppl;
import com.grinnotech.patientsorig.config.security.MongoUserDetails;
import com.grinnotech.patientsorig.dao.ContactRepository;
import com.grinnotech.patientsorig.dao.authorities.RequireAdminEmployeeAuthority;
import com.grinnotech.patientsorig.model.ContactMethod;
import com.grinnotech.patientsorig.util.ValidationMessages;
import com.grinnotech.patientsorig.util.ValidationMessagesResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;

/**
 *
 * @author Jacek Sztajnke
 */
@Service
@RequireAdminEmployeeAuthority
public class ContactService extends AbstractService<ContactMethod> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ContactRepository contactRepository;

	public ContactService(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

	@ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<ContactMethod> read(ExtDirectStoreReadRequest request) {
        List<ContactMethod> list = contactRepository.findAllActive(getSpringSort(request));
        LOGGER.debug("read size:[" + list.size() + "]");
        return new ExtDirectStoreResult<>(list);
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ExtDirectStoreResult<ContactMethod> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, ContactMethod contact)
            throws NotFoundException {
        ExtDirectStoreResult<ContactMethod> result = new ExtDirectStoreResult<>();

        LOGGER.debug("destroy 1");
        Optional<ContactMethod> oOld = contactRepository.findById(contact.getId());
        ContactMethod old = oOld.orElseThrow(new NotFoundExceptionSuppl("ContactMethod id={} not found", contact.getId()));

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
    public ValidationMessagesResult<ContactMethod> update(@AuthenticationPrincipal MongoUserDetails userDetails, ContactMethod contact)
            throws NotFoundException {
        List<ValidationMessages> violations = validateEntity(contact, userDetails.getLocale());

        ValidationMessagesResult<ContactMethod> result = new ValidationMessagesResult<>(contact);
        result.setValidations(violations);

        LOGGER.debug("update 1: " + contact.toString());
        if (violations.isEmpty()) {
            Optional<ContactMethod> old = contactRepository.findById(contact.getId());
            ifPresent(old, contactMethod -> {
                contactMethod.setId(null);
                contactMethod.setActive(false);
                contactRepository.save(contactMethod);
                LOGGER.debug("update 2 " + contactMethod.getId());
                try {
                    setAttrsForUpdate(contact, userDetails, contactMethod);
                } catch (NotFoundException e) {
                    sneakyThrow(e);
                }
            }).orElse(() -> {
                try {
                    setAttrsForCreate(contact, userDetails);
                } catch (NotFoundException e) {
                    sneakyThrow(e);
                }
            });

            contactRepository.save(contact);
            LOGGER.debug("update 3");
        }

        LOGGER.debug("update end");
        return result;
    }

    protected List<ValidationMessages> validateEntity(ContactMethod contact, Locale locale) {
        List<ValidationMessages> validations = super.validateEntity(contact);

        // TODO:
        return validations;
    }
}
