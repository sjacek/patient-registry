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

import static com.grinnotech.patientsorig.model.address.Utils.address;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

import com.grinnotech.patientsorig.model.Patient;
import com.grinnotech.patientsorig.service.ExportController.Attributes;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// change the file name
		response.setHeader("Content-Disposition", "attachment; filename=\"patients.pdf\"");

		List<Patient> patients = (List<Patient>) model.get(Attributes.patients.name());
		document.add(new Paragraph("Generated patients list " + LocalDate.now()));

		//        PdfPTable table = new PdfPTable(users.stream().findAny().get().getColumnCount());
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100.0f);
		table.setSpacingBefore(10);

		// define font for table header row
		Font font = FontFactory.getFont(FontFactory.TIMES);
		font.setColor(BaseColor.WHITE);

		// define table header cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.DARK_GRAY);
		cell.setPadding(5);

		MessageSource msgSrc = new MessageSource(ENGLISH);

		// write table header
		cell.setPhrase(new Phrase(msgSrc.getMessage("patient_fullname"), font));
		table.addCell(cell);

		cell.setPhrase(new Phrase(msgSrc.getMessage("patient_birthday"), font));
		table.addCell(cell);

		cell.setPhrase(new Phrase(msgSrc.getMessage("patient_pesel"), font));
		table.addCell(cell);

		cell.setPhrase(new Phrase(msgSrc.getMessage("patient_address"), font));
		table.addCell(cell);

		cell.setPhrase(new Phrase(msgSrc.getMessage("patient_status"), font));
		table.addCell(cell);

		for (Patient patient : patients) {
			table.addCell(format("%s %s", patient.getFirstName(), patient.getLastName()));
			table.addCell(patient.getBirthday().toString());
			table.addCell(patient.getPesel());
			table.addCell(address(patient));
			table.addCell(patient.getStatus().name());
		}

		document.add(table);
	}
}
