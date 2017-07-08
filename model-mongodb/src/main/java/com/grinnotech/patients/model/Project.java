/*
 * Copyright (C) 2016 Pivotal Software, Inc.
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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinnotech.patients.domain.AbstractPersistable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.TemporalType.DATE;

/**
 *
 * @author Jacek Sztajnke
 */
@Document
@Model(value = "Patients.model.Project",
        createMethod = "projectService.update",
        readMethod = "projectService.read",
        updateMethod = "projectService.update",
        destroyMethod = "projectService.destroy",
        paging = true,
        identifier = "uuid")
@JsonInclude(NON_NULL)
@Getter
@Setter
public class Project extends AbstractPersistable {

    @NotBlank(message = "{fieldrequired}")
    @Indexed(unique=true)
    private String name;

    @NotNull(message = "{fieldrequired}")
    @Indexed
    @ModelField(dateFormat = "c")
    @JsonFormat(shape=STRING)
    @Temporal(DATE)
    private Date start;

    @NotNull(message = "{fieldrequired}")
    @Indexed
    @ModelField(dateFormat = "c")
    @JsonFormat(shape=STRING)
    @Temporal(DATE)
    private Date end;

    @ModelField
    @Transient
    private Organization organization;

    private String organizationId;

    private List<String> coordinatorsIds;

    @JsonIgnore
    private List<User> coordinators;
    
    private List<String> employeesIds;

    @JsonIgnore
    private List<User> employees;
    
    private List<String> participantsIds;

    @JsonIgnore
    private List<Patient> participants;

    private ProjectStatus status;
}
