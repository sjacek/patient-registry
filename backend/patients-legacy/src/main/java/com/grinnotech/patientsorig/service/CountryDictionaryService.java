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
import com.grinnotech.patientsorig.config.security.MongoUserDetails;
import com.grinnotech.patientsorig.dao.CountryDictionaryRepository;
import com.grinnotech.patientsorig.dao.authorities.RequireAdminEmployeeAuthority;
import com.grinnotech.patientsorig.dao.authorities.RequireEmployeeAuthority;
import com.grinnotech.patientsorig.model.CountryDictionary;
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
public class CountryDictionaryService extends AbstractService<CountryDictionary> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final CountryDictionaryRepository addressDictionaryRepository;

	public CountryDictionaryService(CountryDictionaryRepository addressDictionaryRepository) {
		this.addressDictionaryRepository = addressDictionaryRepository;
	}

	@ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<CountryDictionary> read(ExtDirectStoreReadRequest request) {
        LOGGER.debug("read 1");
        List<CountryDictionary> list = addressDictionaryRepository.findAllCountriesActive(getSpringSort(request));
        LOGGER.debug("read size:[" + list.size() + "]");
        return new ExtDirectStoreResult<>(list);
    }

    @ExtDirectMethod(STORE_MODIFY)
    @RequireEmployeeAuthority
    public ExtDirectStoreResult<CountryDictionary> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, CountryDictionary addressDictionary)
            throws NotFoundException {
        ExtDirectStoreResult<CountryDictionary> result = new ExtDirectStoreResult<>();

        LOGGER.debug("destroy 1");
        Optional<CountryDictionary> oOld = addressDictionaryRepository.findById(addressDictionary.getId());
        CountryDictionary old = oOld.orElseThrow(() -> new NotFoundException("CountryDictionary id={} not found", addressDictionary.getId()));

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

        LOGGER.debug("update 1: " + addressDictionary.toString());
        if (violations.isEmpty()) {
            Optional<CountryDictionary> old = addressDictionaryRepository.findById(addressDictionary.getId());
            ifPresent(old, countryDictionary -> {
                countryDictionary.setId(null);
                countryDictionary.setActive(false);
                addressDictionaryRepository.save(countryDictionary);
                LOGGER.debug("update 2 " + countryDictionary);
                try {
                    setAttrsForUpdate(addressDictionary, userDetails, countryDictionary);
                } catch (NotFoundException e) {
                    sneakyThrow(e);
                }
            }).orElse(() -> {
                    try {
                        setAttrsForCreate(addressDictionary, userDetails);
                    } catch (NotFoundException e) {
                        sneakyThrow(e);
                    }
                });

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
