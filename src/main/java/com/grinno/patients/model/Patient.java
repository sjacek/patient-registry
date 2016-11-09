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

import ch.rasc.extclassgenerator.Model;
import ch.rasc.extclassgenerator.ModelField;
import com.fasterxml.jackson.annotation.JsonFormat;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.grinno.patients.domain.AbstractPersistable;
import java.util.Date;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author jsztajnke
 */
@Document
@CompoundIndexes({
    @CompoundIndex(name = "lastName_firstName", def = "{'lastName': 1, 'firstName': 1}")
})
@Model(value = "Patients.model.Patient",
        readMethod = "patientService.read",
        createMethod = "patientService.update",
        updateMethod = "patientService.update",
        destroyMethod = "patientService.destroy",
        paging = true,
        identifier = "uuid"
)
@JsonInclude(NON_NULL)
public class Patient extends AbstractPersistable {

    @NotBlank(message = "{fieldrequired}")
    @Indexed
    private String firstName;

    private String secondName;

    @NotBlank(message = "{fieldrequired}")
    @Indexed
    private String lastName;

    @NotBlank(message = "{fieldrequired}")
    @Indexed
    private String pesel;

    @NotNull(message = "{fieldrequired}")
    @Indexed
    @ModelField(dateFormat = "c")
    @JsonFormat(shape=STRING)
    @Temporal(DATE)
    private Date birthday;

//    @Email(message = "{fieldrequired}")
//    private String email;

    public Patient() {
    }

    public Patient(String id, String firstName, String secondName, String lastName, String pesel, Date birthday) {
        super.setId(id);
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.birthday = birthday;
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

    public Date getBirthday() {
        return birthday;
    }
    
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
    
    @Override
    public String toString() {
        return getId() + "[" + getFirstName() + ", " + getLastName() + ", " + getPesel() + "]";
    }

}
