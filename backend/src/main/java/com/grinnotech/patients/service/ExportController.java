/*
 * Copyright (C) 2017 Jacek Sztajnke
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;

@Controller
@RequestMapping(path = "/export")
public class ExportController {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    public ExportController(PatientService patientService) {
        this.patientService = patientService;
    }

    public enum Attributes {
        patients
    }

    private final PatientService patientService;

    @RequestMapping(path = "/patients", method = RequestMethod.GET)
    public String patients(@RequestParam("organizationId") String organizationId,
                           @RequestParam("filter") String filter,
                           @RequestParam("sort") String sort,
                           @RequestParam("dir") String dir,
                           Model model) {
        logger.trace("/export/patients filter:{}, sort:{}, dir:{}", filter, sort, dir);
        List<Sort.Order> list = new ArrayList<Sort.Order>() {{
            add(new Sort.Order(ASC, sort));
        }};
        model.addAttribute(Attributes.patients.name(), patientService.findPatients(organizationId, filter, by(list)));
        return "";
    }
}
