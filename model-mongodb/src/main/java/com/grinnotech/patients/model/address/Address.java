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
package com.grinnotech.patients.model.address;

import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotBlank;

import ch.rasc.extclassgenerator.Model;
import ch.rasc.extclassgenerator.ModelField;

/**
 * @author Jacek Sztajnke
 */
@Model(value = "Patients.model.Address")
public class Address {

	@Indexed
	@ModelField(defaultValue = "Pl")
	@NotBlank(message = "{fieldrequired}")
	private String country;

	//    @ModelField(customType = "zipcode")
	private String zipCode;

	@Indexed
	private String city;

	private String address1;

	private String address2;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}
}
