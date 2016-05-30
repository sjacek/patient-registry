/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grinno.patients.service;

import com.grinno.patients.model.Patient;
import com.grinno.patients.vo.Result;
import java.util.List;

/**
 *
 * @author jacek
 */
public interface PatientService {

    public Result<Patient> update(String idPatient, String firstName, String secondName, String lastName, String pesel);
    public Result<Patient> destroy (String idPatient);
    public Result<Patient> read(String idPatient);
    public Result<List<Patient>> findAll();
}
