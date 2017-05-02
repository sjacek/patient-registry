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

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import com.grinnotech.patients.config.security.MongoUserDetails;
import com.grinnotech.patients.dao.CountryDictionaryRepository;
import com.grinnotech.patients.dao.authorities.RequireAdminEmployeeAuthority;
import com.grinnotech.patients.dao.authorities.RequireEmployeeAuthority;
import com.grinnotech.patients.model.CountryDictionary;
import com.grinnotech.patients.util.ValidationMessages;
import com.grinnotech.patients.util.ValidationMessagesResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Locale;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import static com.grinnotech.patients.util.QueryUtil.getSpringSort;

/**
 *
 * @author Jacek Sztajnke
 */
@Service
@Cacheable("main")
@RequireAdminEmployeeAuthority
public class CountryDictionaryService extends AbstractService<CountryDictionary> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private CountryDictionaryRepository addressDictionaryRepository;

    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<CountryDictionary> read(ExtDirectStoreReadRequest request) {
        LOGGER.debug("read 1");
        List<CountryDictionary> list = addressDictionaryRepository.findAllCountriesActive(getSpringSort(request));
        LOGGER.debug("read size:[" + list.size() + "]");
        return new ExtDirectStoreResult<>(list);
    }

    @ExtDirectMethod(STORE_MODIFY)
    @RequireEmployeeAuthority
    public ExtDirectStoreResult<CountryDictionary> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, CountryDictionary addressDictionary) {
        ExtDirectStoreResult<CountryDictionary> result = new ExtDirectStoreResult<>();

        LOGGER.debug("destroy 1");
        CountryDictionary old = addressDictionaryRepository.findOne(addressDictionary.getId());

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
    public ValidationMessagesResult<CountryDictionary> update(@AuthenticationPrincipal MongoUserDetails userDetails, CountryDictionary addressDictionary) {
        List<ValidationMessages> violations = validateEntity(addressDictionary, userDetails.getLocale());

        ValidationMessagesResult<CountryDictionary> result = new ValidationMessagesResult<>(addressDictionary);
        result.setValidations(violations);
//

        LOGGER.debug("update 1: " + addressDictionary.toString());
        if (violations.isEmpty()) {
            CountryDictionary old = addressDictionaryRepository.findOne(addressDictionary.getId());
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

    protected List<ValidationMessages> validateEntity(CountryDictionary addressDictionary, Locale locale) {
        List<ValidationMessages> validations = super.validateEntity(addressDictionary);

        // TODO:
        return validations;
    }
}
