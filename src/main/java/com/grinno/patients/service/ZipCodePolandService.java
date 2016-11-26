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
import ch.ralscha.extdirectspring.filter.StringFilter;
import com.grinno.patients.config.security.MongoUserDetails;
import com.grinno.patients.dao.ZipCodePolandRepository;
import com.grinno.patients.dao.authorities.RequireAnyAuthority;
import com.grinno.patients.dao.authorities.RequireEmployeeAuthority;
import com.grinno.patients.model.User;
import com.grinno.patients.model.ZipCodePoland;
import com.grinno.patients.util.QueryUtil;
import static com.grinno.patients.util.QueryUtil.getSpringSort;
import com.grinno.patients.util.ValidationMessages;
import com.grinno.patients.util.ValidationMessagesResult;
import com.grinno.patients.util.ValidationUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Locale;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import static com.grinno.patients.util.QueryUtil.getPageable;

/**
 *
 * @author Jacek Sztajnke
 */
@Service
@RequireAnyAuthority
public class ZipCodePolandService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ZipCodePolandRepository zipCodePolandRepository;

    @Autowired
    private Validator validator;

    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<ZipCodePoland> read(ExtDirectStoreReadRequest request) {

        StringFilter filter = request.getFirstFilterForField("filter");

        Page<ZipCodePoland> page = (filter != null)
                ? zipCodePolandRepository.findAllWithFilterActive(filter.getValue(), getPageable(request))
                : zipCodePolandRepository.findAllActive(getPageable(request));

        LOGGER.debug("read size:[" + page.getSize() + "]");
        return new ExtDirectStoreResult<>(page.getTotalElements(), page.getContent());
    }

    @ExtDirectMethod(STORE_MODIFY)
    @RequireEmployeeAuthority
    public ExtDirectStoreResult<ZipCodePoland> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, ZipCodePoland zipCodePoland) {
        ExtDirectStoreResult<ZipCodePoland> result = new ExtDirectStoreResult<>();

        LOGGER.debug("destroy 1");
        ZipCodePoland old = zipCodePolandRepository.findOne(zipCodePoland.getId());

        old.setId(null);
        old.setActive(false);
        zipCodePolandRepository.save(old);
        LOGGER.debug("destroy 2 " + old.getId());

        setAttrsForDelete(zipCodePoland, userDetails, old);
        zipCodePolandRepository.save(zipCodePoland);
        LOGGER.debug("destroy end");
        return result.setSuccess(true);
    }

    @ExtDirectMethod(STORE_MODIFY)
    @RequireEmployeeAuthority
    public ValidationMessagesResult<ZipCodePoland> update(@AuthenticationPrincipal MongoUserDetails userDetails, ZipCodePoland zipCodePoland) {
        List<ValidationMessages> violations = validateEntity(zipCodePoland, userDetails.getLocale());

        ValidationMessagesResult<ZipCodePoland> result = new ValidationMessagesResult<>(zipCodePoland);
        result.setValidations(violations);
//

        LOGGER.debug("update 1: " + zipCodePoland.toString());
        if (violations.isEmpty()) {
            ZipCodePoland old = zipCodePolandRepository.findOne(zipCodePoland.getId());
            if (old != null) {
                old.setId(null);
                old.setActive(false);
                zipCodePolandRepository.save(old);
                LOGGER.debug("update 2 " + old);
                setAttrsForUpdate(zipCodePoland, userDetails, old);
            }
            else {
                setAttrsForCreate(zipCodePoland, userDetails);
            }

            zipCodePolandRepository.save(zipCodePoland);
            LOGGER.debug("update 3");
        }
        
        LOGGER.debug("update end");
        return result;
    }

    private List<ValidationMessages> validateEntity(ZipCodePoland zipCodePoland, Locale locale) {
        List<ValidationMessages> validations = ValidationUtil.validateEntity(validator, zipCodePoland);

        // TODO:
        return validations;
    }
    
}
