/*
 * Copyright (C) 2016 jacek
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
package com.grinno.patients.ajax;

import com.grinno.patients.dao.PatientRepository;
import com.grinno.patients.model.Patient;
import com.grinno.patients.vo.Result;
import com.grinno.patients.vo.ResultFactory;
import java.util.List;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jacek
 */
@Controller
@RequestMapping("/patient")
public class PatientHandler extends AbstractHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientHandler.class);

    @Autowired
    public PatientRepository patientRepository;
    
    @RequestMapping(value="/find", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public String find(@RequestParam(value = "id", required = true) String idPatient, HttpServletRequest request) {
/*        User sessionUser = getSessionUser(request);
        if (sessionUser == null)
            return getJsonErrorMsg("User is not logged on");
*/        
        Patient patient = patientRepository.findOneById(idPatient);
        return getJsonSuccessData(patient);
//        return getJsonErrorMsg(ar.getMsg());
    }

    @RequestMapping(value="/store", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public String store(@RequestParam(value="data", required = true) String jsonData, HttpServletRequest request) {
        LOGGER.debug("PatientHandler.store(" + jsonData + ")");
/*        User sessionUser = getSessionUser(request);
        if (sessionUser == null)
            return getJsonErrorMsg("User is not logged on");
*/
        JsonObject jsonObj = parseJsonObject(jsonData);
        String pesel = jsonObj.getString("pesel");
        Patient patient = new Patient(
                jsonObj.getString("firstName"),
                jsonObj.getString("secondName"),
                jsonObj.getString("lastName"),
                pesel);
        patientRepository.insert(patient);
//        Result<Patient> result = ResultFactory.getSuccessResult(patientRepository.findOneByPesel(pesel));
        Result<Patient> result = ResultFactory.getSuccessResult(patient);
        return getJsonSuccessData(result.getData());
    }

    @RequestMapping(value="/findAll", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public String findAll(HttpServletRequest request) {
        LOGGER.debug("PatientHandler.findAll()");
/*        User sessionUser = getSessionUser(request);
        if (sessionUser == null) {
            return getJsonErrorMsg("User is not logged on");
        }
*/
        Result<List<Patient>> result = ResultFactory.getSuccessResult(patientRepository.findAll());
        
        if (result.isSuccess())
            return getJsonSuccessData(result.getData());
        return getJsonErrorMsg(result.getMsg());
    }

    @RequestMapping(value="/remove", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public String remove(@RequestParam(value = "data", required = true) String jsonData, HttpServletRequest request) {
/*        User sessionUser = getSessionUser(request);
        if (sessionUser == null) {
            return getJsonErrorMsg("User is not logged on");
        }
*/
        JsonObject jsonObj = parseJsonObject(jsonData);
        Result<Patient> result = ResultFactory.getSuccessResult(patientRepository.remove(jsonObj.getString("id")));

        if (result.isSuccess())
            return getJsonSuccessData(result.getData());
        return getJsonErrorMsg(result.getMsg());
    }
}
