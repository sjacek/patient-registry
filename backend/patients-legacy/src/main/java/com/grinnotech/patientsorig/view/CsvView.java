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
package com.grinnotech.patientsorig.view;

import com.grinnotech.patients.model.Patient;
import com.grinnotech.patientsorig.service.ExportController.Attributes;

import org.jetbrains.annotations.NotNull;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static java.util.Locale.ENGLISH;

public class CsvView extends AbstractCsvView {

    @Override
    protected void buildCsvDocument(@NotNull Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (model.isEmpty())
            return;
        response.setHeader("Content-Disposition", "attachment; filename=\"patients.csv\"");

        List<Patient> patients = (List<Patient>) model.get(Attributes.patients.name());
        MessageSource msgSrc = new MessageSource(ENGLISH);
        String[] header = {
                msgSrc.getMessage("patient_fullname"),
                msgSrc.getMessage("patient_birthday"),
                msgSrc.getMessage("patient_pesel"),
                msgSrc.getMessage("patient_address"),
                msgSrc.getMessage("patient_status")
        };
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(header);

        for (Patient patient : patients) csvWriter.write(patient, header);
        csvWriter.close();
    }
}
