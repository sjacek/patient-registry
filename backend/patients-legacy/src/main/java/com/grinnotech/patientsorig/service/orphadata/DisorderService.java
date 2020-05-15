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
package com.grinnotech.patientsorig.service.orphadata;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import static com.grinnotech.patientsorig.util.QueryUtil.getSpringSort;

import com.grinnotech.patientsorig.dao.authorities.RequireEmployeeAuthority;
import com.grinnotech.patientsorig.dao.orphadata.DisorderRepository;
import com.grinnotech.patientsorig.model.orphadata.Disorder;
import com.grinnotech.patientsorig.service.AbstractService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import ch.ralscha.extdirectspring.filter.StringFilter;

/**
 * @author Jacek Sztajnke
 */
@Service
@RequireEmployeeAuthority
public class DisorderService extends AbstractService<Disorder> {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DisorderRepository disorderRepository;

	public DisorderService(DisorderRepository disorderRepository) {
		this.disorderRepository = disorderRepository;
	}

	@ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<Disorder> read(ExtDirectStoreReadRequest request) {
        StringFilter stringFilter = request.getFirstFilterForField("filter");
        String filter = stringFilter != null ? stringFilter.getValue() : "";
        Collection<Disorder> collection = findAllDisorders(filter, getSpringSort(request));
        return new ExtDirectStoreResult<>(collection);
    }

    private Collection<Disorder> findAllDisorders(String filter, Sort sort) {

        Collection<Disorder> collection = filter.isEmpty()
                ? disorderRepository.findAll(sort)
                : disorderRepository.findAllWithFilter(filter, sort);

        logger.debug("findPatients size:[" + collection.size() + "]");

        return collection;
    }

//    @ExtDirectMethod(STORE_MODIFY)
//    @RequireEmployeeAuthority
//    public ExtDirectStoreResult<Disorder> destroy(@AuthenticationPrincipal MongoUserDetails userDetails, Disorder disorder) {
//        ExtDirectStoreResult<Disorder> result = new ExtDirectStoreResult<>();
//
//        logger.debug("destroy 1");
//        Disorder old = disorderRepository.findOne(disorder.getId());
//
//        old.setId(null);
//        old.setActive(false);
//        disorderRepository.save(old);
//        logger.debug("destroy 2 " + old.getId());
//
//        setAttrsForDelete(disorder, userDetails, old);
//        disorderRepository.save(disorder);
//        logger.debug("destroy end");
//        return result.setSuccess(true);
//    }

//    @ExtDirectMethod(STORE_MODIFY)
//    @RequireEmployeeAuthority
//    public ValidationMessagesResult<Disorder> update(@AuthenticationPrincipal MongoUserDetails userDetails, Disorder disorder) {
//        logger.debug("update 1: " + disorder.toString());
//        ValidationMessagesResult<Disorder> result = new ValidationMessagesResult<>(disorder);
//
//        Disorder old = disorderRepository.findOne(disorder.getId());
//        if (old != null) {
//            old.setId(null);
//            old.setActive(false);
//            disorderRepository.save(old);
//            logger.debug("update 2 " + old);
//            setAttrsForUpdate(disorder, userDetails, old);
//        } else {
//            setAttrsForCreate(disorder, userDetails);
//        }
//
//        disorderRepository.save(disorder);
//        logger.debug("update 3");
//
//        logger.debug("update end");
//        return result;
//    }
}
