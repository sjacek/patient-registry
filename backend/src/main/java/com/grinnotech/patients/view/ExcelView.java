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
package com.grinnotech.patients.view;

import com.grinnotech.patients.model.Patient;
import com.grinnotech.patients.service.ExportController;
import com.grinnotech.patients.util.MessageSource;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.grinnotech.patients.model.address.Utils.address;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.getAvailableLocales;

public class ExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(
            Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"patients.xls\"");

        List<Patient> patients = (List<Patient>) model.get(ExportController.Attributes.patients.name());

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("Patients list");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);

        // create header row
        Row header = sheet.createRow(0);
        MessageSource msgSrc = new MessageSource(ENGLISH);
        header.createCell(0).setCellValue(msgSrc.getMessage("patient_fullname"));
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue(msgSrc.getMessage("patient_birthday"));
        header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue(msgSrc.getMessage("patient_pesel"));
        header.getCell(2).setCellStyle(style);
        header.createCell(3).setCellValue(msgSrc.getMessage("patient_address"));
        header.getCell(3).setCellStyle(style);
        header.createCell(4).setCellValue(msgSrc.getMessage("patient_status"));
        header.getCell(4).setCellStyle(style);

        int rowCount = 1;

        for (Patient patient : patients) {
            Row userRow = sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(format("%s %s", patient.getFirstName(), patient.getLastName()));
            userRow.createCell(1).setCellValue(patient.getBirthday().toString());
            userRow.createCell(2).setCellValue(patient.getPesel());
            userRow.createCell(3).setCellValue(address(patient));
            userRow.createCell(4).setCellValue(patient.getStatus().name());
        }
    }
}
