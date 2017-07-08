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

import ch.rasc.extclassgenerator.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.grinnotech.patients.domain.AbstractPersistable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 *
 * @author Jacek Sztajnke
 */
@Document(collection="dic_contactmethod")
@CompoundIndex(name = "locale_method", def = "{'locale': 1, 'method': 1, 'version': 1}", unique = true)
@Model(value = "Patients.model.ContactMethod",
        createMethod = "contactService.update",
        readMethod = "contactService.read",
        updateMethod = "contactService.update",
        destroyMethod = "contactService.destroy",
        paging = true,
        identifier = "uuid")
@JsonInclude(NON_NULL)
@Builder
@Getter
@Setter
public class ContactMethod extends AbstractPersistable {

    @NotBlank(message = "{fieldrequired}")
    private String locale;

    @NotBlank(message = "{fieldrequired}")
    private String method;

    private String description;

    @Override
    public String toString() {
        return getId() + "[" + getMethod() + "]";
    }
}
