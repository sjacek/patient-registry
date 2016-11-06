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
import com.grinno.patients.dao.PatientRepository;
import com.grinno.patients.model.Patient;
import com.grinno.patients.vo.Result;
import com.grinno.patients.vo.ResultFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("patientRestService")
public class PatientRestServiceImpl extends AbstractService implements PatientRestService {

    final protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected PatientRepository patientRepository;
    
    @ExtDirectMethod(STORE_MODIFY)
    @Transactional(readOnly = false, propagation = REQUIRED)
    @Override
    public Result<Patient> update(String idPatient, String firstName, String secondName, String lastName, String pesel) {
        Patient patient = new Patient(idPatient, firstName, secondName, lastName, pesel);
        patientRepository.save(patient);
        return ResultFactory.getSuccessResult(patient);
    }

    @ExtDirectMethod(STORE_MODIFY)
    @Transactional(readOnly = false, propagation = REQUIRED)
    @Override
    public Result<Patient> destroy(String idPatient) {
        Patient patient = patientRepository.findOne(idPatient);
        patientRepository.delete(idPatient);
        String msg = "Patient PESEL=" + patient.getPesel() + " was deleted";
        LOGGER.debug(msg);
        return ResultFactory.getSuccessResultMsg(msg);
    }
    
    @ExtDirectMethod(STORE_READ)
    @Transactional(readOnly = true, propagation = SUPPORTS)
    @Override
    public Result<Patient> read(String idPatient) {
        Patient patient = patientRepository.findOne(idPatient);
        return ResultFactory.getSuccessResult(patient);
    }

    @Transactional(readOnly = true, propagation = SUPPORTS)
    @Override
    public Result<List<Patient>> findAll() {
        return ResultFactory.getSuccessResult(patientRepository.findAll());
    }
    
}
