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

import com.grinnotech.patients.NotFoundException;
import com.grinnotech.patients.model.Patient;
import com.grinnotech.patients.vo.Result;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Jacek Sztajnke
 */
public interface PatientRestService {
    Result<Patient> update(String idPatient, String firstName, String secondName, String lastName, String pesel, Date birthday);
    Result<Patient> destroy (String idPatient) throws NotFoundException;
    Result<Patient> read(String idPatient) throws NotFoundException;
    Result<List<Patient>> findAll();
}
