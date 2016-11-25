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
import com.grinno.patients.config.security.MongoUserDetails;
import com.grinno.patients.dao.ProjectRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.grinno.patients.dao.authorities.RequireEmployeeAuthority;
import com.grinno.patients.model.Project;
import static com.grinno.patients.util.QueryUtil.getSpringSort;

/**
 *
 * @author jacek
 */
@Service
@RequireEmployeeAuthority
public class ProjectService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private Validator validator;

    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<Project> read(ExtDirectStoreReadRequest request) {

        StringFilter filter = request.getFirstFilterForField("filter");
        List<Project> list = projectRepository.findAllActive(getSpringSort(request));

        LOGGER.debug("read size:[" + list.size() + "]");

        return new ExtDirectStoreResult<>(list);
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ExtDirectStoreResult<Project> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, Project project) {
        ExtDirectStoreResult<Project> result = new ExtDirectStoreResult<>();

        LOGGER.debug("destroy 1");
        Project old = projectRepository.findOne(project.getId());

        old.setId(null);
        old.setActive(false);
        projectRepository.save(old);
        LOGGER.debug("destroy 2 " + old.getId());

        setAttrsForDelete(project, userDetails, old);
        projectRepository.save(project);
        LOGGER.debug("destroy end");
        return result.setSuccess(true);
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ValidationMessagesResult<Project> update(@AuthenticationPrincipal MongoUserDetails userDetails, Project project) {
        List<ValidationMessages> violations = validateEntity(project, userDetails.getLocale());

        ValidationMessagesResult<Project> result = new ValidationMessagesResult<>(project);
        result.setValidations(violations);

        Project retProject;
        
        LOGGER.debug("update 1: " + project.toString());
        if (violations.isEmpty()) {
            Project old = projectRepository.findOne(project.getId());
            if (old != null) {
                old.setId(null);
                old.setActive(false);
                projectRepository.save(old);
                setAttrsForUpdate(project, userDetails, old);
            }
            else {
                setAttrsForCreate(project, userDetails);
            }

            retProject = projectRepository.save(project);
        }

        LOGGER.debug("update end");
        return result;
    }

    private List<ValidationMessages> validateEntity(Project project, Locale locale) {
        List<ValidationMessages> validations = ValidationUtil.validateEntity(validator, project);

        return validations;
    }
}
