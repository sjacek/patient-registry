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
package com.grinnotech.patients.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinnotech.patients.domain.AbstractPersistable;
import com.opencsv.bean.CsvBindByName;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

import ch.rasc.extclassgenerator.Model;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jacek Sztajnke
 */
@Document(collection = "dic_countries")
@Model(value = "Patients.model.CountryDictionary", createMethod = "countryDictionaryService.update", readMethod = "countryDictionaryService.read", updateMethod = "countryDictionaryService.update", destroyMethod = "countryDictionaryService.destroy", paging = true, identifier = "uuid")
@JsonInclude(NON_NULL)
@Builder
@Getter
@Setter
public class CountryDictionary extends AbstractPersistable {

	@Indexed
	@CsvBindByName(column = "code", required = true)
	@NotBlank(message = "{fieldrequired}")
	private String code;

	@Indexed
	@CsvBindByName(column = "country_en", required = true)
	@NotBlank(message = "{fieldrequired}")
	private String countryEn;

	@Indexed
	@CsvBindByName(column = "country_pl", required = true)
	@NotBlank(message = "{fieldrequired}")
	private String countryPl;

	@Indexed
	@CsvBindByName(column = "country_de", required = true)
	@NotBlank(message = "{fieldrequired}")
	private String countryDe;
}
