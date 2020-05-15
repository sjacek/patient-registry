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
import static com.grinnotech.patientsorig.util.PeselValidator.peselIsValid;
import static com.grinnotech.patientsorig.util.QueryUtil.getSpringSort;
import static org.apache.commons.lang3.StringUtils.join;

import com.grinnotech.patientsorig.NotFoundException;
import com.grinnotech.patientsorig.config.security.MongoUserDetails;
import com.grinnotech.patientsorig.dao.PatientRepository;
import com.grinnotech.patientsorig.dao.authorities.RequireUserEmployeeAuthority;
import com.grinnotech.patients.model.Patient;
import com.grinnotech.patientsorig.util.ThrowingFunction;
import com.grinnotech.patientsorig.util.ValidationMessages;
import com.grinnotech.patientsorig.util.ValidationMessagesResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import ch.ralscha.extdirectspring.filter.StringFilter;

/**
 * @author Jacek Sztajnke
 */
@Service
@RequireUserEmployeeAuthority
public class PatientService extends AbstractService<Patient> {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final PatientRepository patientRepository;

	private final MessageSource messageSource;

	public PatientService(PatientRepository patientRepository, MessageSource messageSource) {
		this.patientRepository = patientRepository;
		this.messageSource = messageSource;
	}

	@ExtDirectMethod(STORE_READ)
	public ExtDirectStoreResult<Patient> read(ExtDirectStoreReadRequest request) {

		StringFilter organizationFilter = request.getFirstFilterForField("organizationId");
		String organizationId = organizationFilter == null ? "null" : organizationFilter.getValue();
		logger.debug("organizationFilter: {}", organizationId);
		if (organizationFilter == null || organizationFilter.getValue().isEmpty())
			return new ExtDirectStoreResult<>(new ArrayList<>());

		StringFilter stringFilter = request.getFirstFilterForField("filter");
		String filter = stringFilter != null ? stringFilter.getValue() : "";
		Collection<Patient> coll = findPatients(organizationFilter.getValue(), filter, getSpringSort(request));
		return new ExtDirectStoreResult<>(coll);
	}

	Collection<Patient> findPatients(String organizationId, String filter, Sort sort) {

		Collection<Patient> coll = filter.isEmpty() ?
				patientRepository.findByOrganizationIdActive(organizationId, sort) :
				patientRepository.findByOrganizationIdWithFilterActive(organizationId, filter, sort);

		logger.debug("findPatients size:[" + coll.size() + "]");

		return coll;
	}

	@ExtDirectMethod(STORE_MODIFY)
	public ExtDirectStoreResult<Patient> destroy(@AuthenticationPrincipal MongoUserDetails userDetails,
			Patient patient) {
		ExtDirectStoreResult<Patient> result = new ExtDirectStoreResult<>();

		logger.debug("destroy 1");
		Optional<Patient> old = patientRepository.findById(patient.getId());

		old.ifPresent(patient1 -> {
			patient1.setId(null);
			patient1.setActive(false);
			patientRepository.save(patient1);
			logger.debug("destroy 2 " + patient1);

			try {
				setAttrsForDelete(patient, userDetails, patient1);
			} catch (NotFoundException e) {
				logger.warn("Patient {} not found", patient1);
			}
		});
		patientRepository.save(patient);
		logger.debug("destroy end");
		return result.setSuccess(true);
	}

	@ExtDirectMethod(STORE_MODIFY)
	public ValidationMessagesResult<Patient> update(@AuthenticationPrincipal MongoUserDetails userDetails,
			Patient patient) {
		List<ValidationMessages> violations = validateEntity(patient, userDetails.getLocale());

		ValidationMessagesResult<Patient> result = new ValidationMessagesResult<>(patient);
		result.setValidations(violations);

		logger.debug("update 1: " + patient.toString());
		if (violations.isEmpty()) {
			Optional<Patient> old = patientRepository.findById(patient.getId());

			ifPresent(old, patient1 -> {
				patient1.setId(null);
				patient1.setActive(false);
				patientRepository.save(patient1);
				try {
					setAttrsForUpdate(patient, userDetails, patient1);
				} catch (NotFoundException e) {
					ThrowingFunction.sneakyThrow(e);
				}
			}).orElse(() -> {
				try {
					setAttrsForCreate(patient, userDetails);
				} catch (NotFoundException e) {
					ThrowingFunction.sneakyThrow(e);
				}
			});

			patientRepository.save(patient);
		}

		if (logger.isDebugEnabled())
			violations.forEach(msg -> logger.debug("{} : {}", msg.getField(), join(msg.getMessages(), ',')));

		logger.debug("update end");
		return result;
	}

	protected List<ValidationMessages> validateEntity(Patient patient, Locale locale) {
		List<ValidationMessages> validations = super.validateEntity(patient);

		if (!peselIsValid(patient.getPesel())) {
			validations.add(ValidationMessages.builder().field("pesel")
					.message(messageSource.getMessage("patient_pesel_not_valid", null, locale)).build());
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
