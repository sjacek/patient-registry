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
package com.grinnotech.patients.model;

import ch.rasc.extclassgenerator.Model;
import ch.rasc.extclassgenerator.ModelField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinnotech.patients.domain.AbstractPersistable;
import com.grinnotech.patients.model.address.Address;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.TemporalType.DATE;

//import ch.rasc.extclassgenerator.ModelHasMany;

/**
 *
 * @author Jacek Sztajnke
 */
@Document
@CompoundIndex(name = "lastName_firstName", def = "{'lastName': 1, 'firstName': 1}")
@Model(value = "Patients.model.Patient",
        createMethod = "patientService.update",
        readMethod = "patientService.read",
        updateMethod = "patientService.update",
        destroyMethod = "patientService.destroy",
        paging = true,
        identifier = "uuid")
@JsonInclude(NON_NULL)
@Getter
@Setter
public class Patient extends AbstractPersistable {

    @NotBlank(message = "{fieldrequired}")
    @Indexed
    private String firstName;

    private String secondName;

    @NotBlank(message = "{fieldrequired}")
    @Indexed
    private String lastName;

    @NotNull(message = "{fieldrequired}")
    @ModelField
    private Gender gender;
    
    @NotNull(message = "{fieldrequired}")
    @ModelField(defaultValue = "NEW")
    private PatientStatus status;

    private boolean ward;
    
    @NotBlank(message = "{fieldrequired}")
    @Indexed
//    @ModelField(customType = "pesel")
    private String pesel;

    @NotNull(message = "{fieldrequired}")
    @Indexed
    @ModelField(dateFormat = "c")
    @JsonFormat(shape=STRING)
    @Temporal(DATE)
//    @Past
    private Date birthday;

//    @ModelHasOne
//    @ModelField(reference=@ReferenceConfig(type="Patients.model.Address"))
    @ModelField
    private Address address;

//    @ModelHasOne
//    @ModelField(reference=@ReferenceConfig(type="Patients.model.Address"))
    @ModelField
    private Address correspondenceAddress;

//    @ModelField
//    @ch.rasc.bsoncodec.annotation.Transient
////    @javax.persistence.Transient
//    @org.springframework.data.annotation.Transient
//    @NotNull(message = "{fieldrequired}")
//    private Organization organization;

    @NotBlank(message = "{fieldrequired}")
    private String organizationId;
    
//    @ModelHasMany
    private List<Contact> contacts;

    @ModelField
    @NotNull(message = "{fieldrequired}")
    private DisabilityLevel disabilityLevel;
    
    @ModelField(dateFormat = "c")
    @JsonFormat(shape=STRING)
    @Temporal(DATE)
    private Date certificateOfDisabilityIssue;

    private String certificateOfDisabilityIssuingUnit;

    @Indexed
    @ModelField(dateFormat = "c")
    @JsonFormat(shape=STRING)
    @Temporal(DATE)
    private Date certificateOfDisabilityExpiration;

    @ModelField
    private Diagnosis diagnosis;

    @Override
    public String toString() {
        return getId() + "[" + getFirstName() + ", " + getLastName() + ", " + getPesel() + "]";
    }

}
