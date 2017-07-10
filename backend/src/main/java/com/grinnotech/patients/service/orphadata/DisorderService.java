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
package com.grinnotech.patients.service.orphadata;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreReadRequest;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import com.grinnotech.patients.dao.authorities.RequireEmployeeAuthority;
import com.grinnotech.patients.dao.orphadata.DisorderRepository;
import com.grinnotech.patients.model.orphadata.Disorder;
import com.grinnotech.patients.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import static com.grinnotech.patients.util.QueryUtil.getSpringSort;

/**
 * @author Jacek Sztajnke
 */
@Service
@RequireEmployeeAuthority
public class DisorderService extends AbstractService<Disorder> {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private DisorderRepository disorderRepository;

    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<Disorder> read(ExtDirectStoreReadRequest request) {
        logger.debug("read 1");
        List<Disorder> list = disorderRepository .findAll(getSpringSort(request));
        logger.debug("read size:[" + list.size() + "]");
        return new ExtDirectStoreResult<>(list);
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
