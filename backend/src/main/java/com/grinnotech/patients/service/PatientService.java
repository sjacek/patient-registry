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
import ch.ralscha.extdirectspring.filter.StringFilter;
import com.grinnotech.patients.config.security.MongoUserDetails;
import com.grinnotech.patients.dao.PatientRepository;
import com.grinnotech.patients.dao.authorities.RequireUserEmployeeAuthority;
import com.grinnotech.patients.model.Patient;
import com.grinnotech.patients.util.ValidationMessages;
import com.grinnotech.patients.util.ValidationMessagesResult;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Locale;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import static com.grinnotech.patients.util.PeselValidator.peselIsValid;
import static com.grinnotech.patients.util.QueryUtil.getSpringSort;

/**
 *
 * @author jacek
 */
@Service
@Cacheable("main")
@RequireUserEmployeeAuthority
@Log4j
public class PatientService extends AbstractService<Patient> {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MessageSource messageSource;

    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<Patient> read(ExtDirectStoreReadRequest request) {

        StringFilter stringFilter = request.getFirstFilterForField("filter");
        String filter = stringFilter != null ? stringFilter.getValue() : "";
        List<Patient> list = findAllPatients(filter, getSpringSort(request));
        return new ExtDirectStoreResult<>(list);
    }

    public List<Patient> findAllPatients(String filter, Sort sort) {

        List<Patient> list = filter.isEmpty()
                ? patientRepository.findAllActive(sort)
                : patientRepository.findAllWithFilterActive(filter, sort);

        log.debug("findAllPatients size:[" + list.size() + "]");

        return list;
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ExtDirectStoreResult<Patient> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, Patient patient) {
        ExtDirectStoreResult<Patient> result = new ExtDirectStoreResult<>();

        log.debug("destroy 1");
        Patient old = patientRepository.findOne(patient.getId());

        old.setId(null);
        old.setActive(false);
        patientRepository.save(old);
        log.debug("destroy 2 " + old.getId());

        setAttrsForDelete(patient, userDetails, old);
        patientRepository.save(patient);
        log.debug("destroy end");
        return result.setSuccess(true);
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ValidationMessagesResult<Patient> update(@AuthenticationPrincipal MongoUserDetails userDetails, Patient patient) {
        List<ValidationMessages> violations = validateEntity(patient, userDetails.getLocale());

        ValidationMessagesResult<Patient> result = new ValidationMessagesResult<>(patient);
        result.setValidations(violations);

        log.debug("update 1: " + patient.toString());
        if (violations.isEmpty()) {
            Patient old = patientRepository.findOne(patient.getId());
            if (old != null) {
                old.setId(null);
                old.setActive(false);
                patientRepository.save(old);
                setAttrsForUpdate(patient, userDetails, old);
            }
            else {
                setAttrsForCreate(patient, userDetails);
            }

            patientRepository.save(patient);
        }

        log.debug("update end");
        return result;
    }

    protected List<ValidationMessages> validateEntity(Patient patient, Locale locale) {
        List<ValidationMessages> validations = super.validateEntity(patient);

        if (!peselIsValid(patient.getPesel())) {
            ValidationMessages validationError = new ValidationMessages();
            validationError.setField("pesel");
            validationError.setMessage(messageSource.getMessage("patient_pesel_not_valid", null, locale));
            validations.add(validationError);
        }
        return validations;
    }

//    private boolean isEmailUnique(String patientId, String email) {
//        if (StringUtils.hasText(email)) {
//            long count;
//            if (patientId != null) {
//                count = this.mongoDb.getCollection(Patient.class)
//                        .count(Filters.and(
//                                Filters.regex(CPatient.email, "^" + email + "$", "i"),
//                                Filters.ne(CPatient.id, patientId)));
//            } else {
//                count = this.mongoDb.getCollection(Patient.class)
//                        .count(Filters.regex(CPatient.email, "^" + email + "$", "i"));
//            }
//
//            return count == 0;
//        }
//        return true;
//    }
}
