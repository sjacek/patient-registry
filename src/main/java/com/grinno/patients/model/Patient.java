/*
 * Copyright (C) 2016 jsztajnke
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
package com.grinno.patients.model;

import com.grinno.patients.domain.AbstractEntity;
import javax.json.JsonObjectBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author jsztajnke
 */
@Document
public class Patient extends AbstractEntity {

    private static final Logger LOGGER = LoggerFactory.getLogger(Patient.class);

    @Id
    private String id;

    private String firstName;
    private String secondName;
    private String lastName;
    private String pesel;

    public Patient() {
        LOGGER.debug("Patient()");
    }

    public Patient(String firstName, String secondName, String lastName, String pesel) {
        LOGGER.debug("Patient(" + firstName + "," + secondName + "," + lastName + "," + pesel + ")");
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.pesel = pesel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    @Override
    public String toString() {
        return "Patient[" + getFirstName() + ", " + getSecondName() + ", " + getLastName() + ", " + getPesel() + "]";
    }

    @Override
    public void addJson(JsonObjectBuilder builder) {
        builder.add("id", id)
               .add("firstName", firstName)
               .add("secondName", secondName)
               .add("lastName", lastName)
               .add("pesel", pesel);
    }

}
