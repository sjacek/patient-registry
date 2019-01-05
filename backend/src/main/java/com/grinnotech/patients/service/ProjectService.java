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

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import static com.grinnotech.patients.util.OptionalEx.ifPresent;
import static com.grinnotech.patients.util.QueryUtil.getSpringSort;
import static com.grinnotech.patients.util.ThrowingFunction.sneakyThrow;

import com.grinnotech.patients.NotFoundException;
import com.grinnotech.patients.config.security.MongoUserDetails;
import com.grinnotech.patients.dao.ProjectRepository;
import com.grinnotech.patients.dao.authorities.RequireEmployeeAuthority;
import com.grinnotech.patients.model.Project;
import com.grinnotech.patients.util.ValidationMessages;
import com.grinnotech.patients.util.ValidationMessagesResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
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
@RequireEmployeeAuthority
public class ProjectService extends AbstractService<Project> {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private ProjectRepository projectRepository;

	@ExtDirectMethod(STORE_READ)
	public ExtDirectStoreResult<Project> read(ExtDirectStoreReadRequest request) {

		StringFilter filter = request.getFirstFilterForField("filter");
		List<Project> list = projectRepository.findAllActive(getSpringSort(request));

		logger.debug("read size:[" + list.size() + "]");

		return new ExtDirectStoreResult<>(list);
	}

	@ExtDirectMethod(STORE_MODIFY)
	public ExtDirectStoreResult<Project> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, Project project)
			throws NotFoundException {
		ExtDirectStoreResult<Project> result = new ExtDirectStoreResult<>();

		logger.debug("destroy 1");
		Optional<Project> oProject = projectRepository.findById(project.getId());
		Project old = oProject.orElseThrow(() -> new NotFoundException("Patient id={} nod found", project.getId()));

		old.setId(null);
		old.setActive(false);
		projectRepository.save(old);
		logger.debug("destroy 2 " + old.getId());

		setAttrsForDelete(project, userDetails, old);
		projectRepository.save(project);
		logger.debug("destroy end");
		return result.setSuccess(true);
	}

	@ExtDirectMethod(STORE_MODIFY)
	public ValidationMessagesResult<Project> update(@AuthenticationPrincipal MongoUserDetails userDetails,
			Project project) {
		List<ValidationMessages> violations = validateEntity(project, userDetails.getLocale());

		ValidationMessagesResult<Project> result = new ValidationMessagesResult<>(project);
		result.setValidations(violations);

		logger.debug("update 1: " + project.toString());
		if (violations.isEmpty()) {
			Optional<Project> oProject = projectRepository.findById(project.getId());
			ifPresent(oProject, project1 -> {
				project1.setId(null);
				project1.setActive(false);
				projectRepository.save(project1);
				try {
					setAttrsForUpdate(project, userDetails, project1);
				} catch (NotFoundException e) {
					sneakyThrow(e);
				}
			}).orElse(() -> {
				try {
					setAttrsForCreate(project, userDetails);
				} catch (NotFoundException e) {
					sneakyThrow(e);
				}
			});

			projectRepository.save(project);
		}

		logger.debug("update end");
		return result;
	}

	protected List<ValidationMessages> validateEntity(Project project, Locale locale) {
		List<ValidationMessages> validations = super.validateEntity(project);

		return validations;
	}
}
