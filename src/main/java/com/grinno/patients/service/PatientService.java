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
package com.grinno.patients.service;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import ch.ralscha.extdirectspring.filter.StringFilter;
import com.grinno.patients.config.security.RequireUserAuthority;
import com.grinno.patients.dao.PatientRepository;
import com.grinno.patients.model.Patient;
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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import static com.grinno.patients.util.PeselValidator.peselIsValid;

/**
 *
 * @author jacek
 */
@Service
@RequireUserAuthority
public class PatientService {

    public static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final MessageSource messageSource;

    private final Validator validator;

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository, Validator validator, MessageSource messageSource, MailService mailService) {
        this.patientRepository = patientRepository;
        this.messageSource = messageSource;
        this.validator = validator;
    }

    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<Patient> read(ExtDirectStoreReadRequest request) {

        StringFilter filter = request.getFirstFilterForField("filter");
        List<Patient> list = (filter != null)
                ? patientRepository.findAllWithFilter(filter.getValue())
//            patientRepository.findAllWithFilter(filter.getValue(), request.getStart(), request.getLimit())
                : patientRepository.findAll();

        LOGGER.debug("read size:[" + list.size() + "]");

        return new ExtDirectStoreResult<>(list);
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ExtDirectStoreResult<Patient> destroy(Patient destroyPatient) {
        ExtDirectStoreResult<Patient> result = new ExtDirectStoreResult<>();

        LOGGER.debug("destroy 1");
        patientRepository.delete(destroyPatient);
//        mongoDb.getCollection(Patient.class).deleteOne(Filters.eq(CPatient.id, destroyPatient.getId()));
        result.setSuccess(Boolean.TRUE);

        LOGGER.debug("destroy end");
        return result;
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ValidationMessagesResult<Patient> update(Patient patient, Locale locale) {
        List<ValidationMessages> violations = validateEntity(patient, locale);

        LOGGER.debug("update 1: " + patient.toString());
        if (violations.isEmpty()) {
            LOGGER.debug("update 2");
//            patientRepository.updateFirst(patient);
            patientRepository.save(patient);
            /*            
            LOGGER.debug("update 2");
            List<Bson> updates = new ArrayList<>();
//            updates.add(Updates.set(CPatient.email, patient.getEmail()));
            updates.add(Updates.set(CPatient.firstName, patient.getFirstName()));
            updates.add(Updates.set(CPatient.lastName, patient.getLastName()));
            updates.add(Updates.set(CPatient.pesel, patient.getPesel()));

            LOGGER.debug("update 3");
            mongoDb.getCollection(Patient.class).updateOne(Filters.eq(CPatient.id, patient.getId()), Updates.combine(updates), new UpdateOptions().upsert(true));
            LOGGER.debug("update 4");
            return new ValidationMessagesResult<>(patient);
             */        }

        ValidationMessagesResult<Patient> result = new ValidationMessagesResult<>(patient);
        result.setValidations(violations);
        LOGGER.debug("update end");
        return result;
    }

    private List<ValidationMessages> validateEntity(Patient patient, Locale locale) {
        List<ValidationMessages> validations = ValidationUtil.validateEntity(validator, patient);

        if (!peselIsValid(patient.getPesel())) {
            ValidationMessages validationError = new ValidationMessages();
            validationError.setField("pesel");
            validationError.setMessage(messageSource.getMessage("patient_pesel_not_valid", null, locale));
            validations.add(validationError);
        }
//        if (!isEmailUnique(patient.getId(), patient.getEmail())) {
//            ValidationMessages validationError = new ValidationMessages();
//            validationError.setField(CPatient.email);
//            validationError.setMessage(this.messageSource.getMessage("patient_emailtaken", null, locale));
//            validations.add(validationError);
//        }
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
