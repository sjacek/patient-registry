/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grinno.patients.service;

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
@Service("patientService")
public class PatientServiceImpl extends AbstractService implements PatientService {

    final protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected PatientRepository patientRepository;
    
    @Transactional(readOnly = false, propagation = REQUIRED)
    @Override
    public Result<Patient> store(String idPatient, String firstName, String secondName, String lastName, String pesel) {
        Patient patient = new Patient(firstName, secondName, lastName, pesel);
        if (idPatient == null) {
            patientRepository.insert(patient);
        } else {
            patientRepository.updateFirst(idPatient, patient);
        }
        return ResultFactory.getSuccessResult(patient);
    }

    @Transactional(readOnly = false, propagation = REQUIRED)
    @Override
    public Result<Patient> remove(String idPatient) {
        Patient patient = patientRepository.remove(idPatient);
        String msg = "Patient PESEL=" + patient.getPesel() + " was deleted";
        LOGGER.info(msg);
        return ResultFactory.getSuccessResultMsg(msg);
    }
    
    @Transactional(readOnly = true, propagation = SUPPORTS)
    @Override
    public Result<Patient> find(String idPatient) {
        Patient patient = patientRepository.findOneById(idPatient);
        return ResultFactory.getSuccessResult(patient);
    }

    @Transactional(readOnly = true, propagation = SUPPORTS)
    @Override
    public Result<List<Patient>> findAll() {
        return ResultFactory.getSuccessResult(patientRepository.findAll());
    }
    
}
