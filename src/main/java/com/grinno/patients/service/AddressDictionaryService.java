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
import com.grinno.patients.dao.AddressDictionaryRepository;
import com.grinno.patients.dao.authorities.RequireAnyAuthority;
import com.grinno.patients.dao.authorities.RequireEmployeeAuthority;
import com.grinno.patients.model.AddressDictionary;
import com.grinno.patients.util.ValidationMessages;
import com.grinno.patients.util.ValidationMessagesResult;
import com.grinno.patients.util.ValidationUtil;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Locale;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import static com.grinno.patients.util.QueryUtil.getSpringSort;

/**
 *
 * @author Jacek Sztajnke
 */
@Service
@RequireAnyAuthority
public class AddressDictionaryService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private AddressDictionaryRepository addressDictionaryRepository;

    @Autowired
    private Validator validator;

    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<AddressDictionary> read(ExtDirectStoreReadRequest request) {
        LOGGER.debug("read 1");
        List<AddressDictionary> list = addressDictionaryRepository.findAllCountriesActive(getSpringSort(request));
        LOGGER.debug("read size:[" + list.size() + "]");
        return new ExtDirectStoreResult<>(list);
    }

    @ExtDirectMethod(STORE_MODIFY)
    @RequireEmployeeAuthority
    public ExtDirectStoreResult<AddressDictionary> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, AddressDictionary addressDictionary) {
        ExtDirectStoreResult<AddressDictionary> result = new ExtDirectStoreResult<>();

        LOGGER.debug("destroy 1");
        AddressDictionary old = addressDictionaryRepository.findOne(addressDictionary.getId());

        old.setId(null);
        old.setActive(false);
        addressDictionaryRepository.save(old);
        LOGGER.debug("destroy 2 " + old.getId());

        setAttrsForDelete(addressDictionary, userDetails, old);
        addressDictionaryRepository.save(addressDictionary);
        LOGGER.debug("destroy end");
        return result.setSuccess(true);
    }

    @ExtDirectMethod(STORE_MODIFY)
    @RequireEmployeeAuthority
    public ValidationMessagesResult<AddressDictionary> update(@AuthenticationPrincipal MongoUserDetails userDetails, AddressDictionary addressDictionary) {
        List<ValidationMessages> violations = validateEntity(addressDictionary, userDetails.getLocale());

        ValidationMessagesResult<AddressDictionary> result = new ValidationMessagesResult<>(addressDictionary);
        result.setValidations(violations);
//

        LOGGER.debug("update 1: " + addressDictionary.toString());
        if (violations.isEmpty()) {
            AddressDictionary old = addressDictionaryRepository.findOne(addressDictionary.getId());
            if (old != null) {
                old.setId(null);
                old.setActive(false);
                addressDictionaryRepository.save(old);
                LOGGER.debug("update 2 " + old);
                setAttrsForUpdate(addressDictionary, userDetails, old);
            }
            else {
                setAttrsForCreate(addressDictionary, userDetails);
            }

            addressDictionaryRepository.save(addressDictionary);
            LOGGER.debug("update 3");
        }
        
        LOGGER.debug("update end");
        return result;
    }

    private List<ValidationMessages> validateEntity(AddressDictionary addressDictionary, Locale locale) {
        List<ValidationMessages> validations = ValidationUtil.validateEntity(validator, addressDictionary);

        // TODO:
        return validations;
    }
}
