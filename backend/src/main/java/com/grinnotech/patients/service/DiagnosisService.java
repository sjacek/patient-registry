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
import com.grinnotech.patients.dao.DiagnosisRepository;
import com.grinnotech.patients.dao.authorities.RequireEmployeeAuthority;
import com.grinnotech.patients.model.Diagnosis;
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
@RequireEmployeeAuthority
public class DiagnosisService extends AbstractService<Diagnosis> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<Diagnosis> read(ExtDirectStoreReadRequest request) {
        List<Diagnosis> list = diagnosisRepository.findAllActive(getSpringSort(request));
        LOGGER.debug("read size:[" + list.size() + "]");
        return new ExtDirectStoreResult<>(list);
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ExtDirectStoreResult<Diagnosis> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, Diagnosis diagnosis) {
        ExtDirectStoreResult<Diagnosis> result = new ExtDirectStoreResult<>();

        LOGGER.debug("destroy 1");
        Diagnosis old = diagnosisRepository.findOne(diagnosis.getId());

        old.setId(null);
        old.setActive(false);
        diagnosisRepository.save(old);
        LOGGER.debug("destroy 2 " + old.getId());

        setAttrsForDelete(diagnosis, userDetails, old);
        diagnosisRepository.save(diagnosis);
        LOGGER.debug("destroy end");
        return result.setSuccess(true);
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ValidationMessagesResult<Diagnosis> update(@AuthenticationPrincipal MongoUserDetails userDetails, Diagnosis diagnosis) {
        List<ValidationMessages> violations = validateEntity(diagnosis, userDetails.getLocale());

        ValidationMessagesResult<Diagnosis> result = new ValidationMessagesResult<>(diagnosis);
        result.setValidations(violations);
//

        LOGGER.debug("update 1: " + diagnosis.toString());
        if (violations.isEmpty()) {
            Diagnosis old = diagnosisRepository.findOne(diagnosis.getId());
            if (old != null) {
                old.setId(null);
                old.setActive(false);
                diagnosisRepository.save(old);
                LOGGER.debug("update 2 " + old);
                setAttrsForUpdate(diagnosis, userDetails, old);
            }
            else {
                setAttrsForCreate(diagnosis, userDetails);
            }

            diagnosisRepository.save(diagnosis);
            LOGGER.debug("update 3");
        }
        
        LOGGER.debug("update end");
        return result;
    }

    protected List<ValidationMessages> validateEntity(Diagnosis diagnosis, Locale locale) {
        List<ValidationMessages> validations = super.validateEntity(diagnosis);

        // TODO:
        return validations;
    }
}
