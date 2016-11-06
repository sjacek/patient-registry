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
package com.grinno.patients.model;

import ch.rasc.extclassgenerator.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.grinno.patients.domain.AbstractPersistable;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author jacek
 */
@Document
@Model(value = "Patients.model.Dictionary",
//        readMethod = "dictionaryService.read",
//        createMethod = "dictionaryService.update",
//        updateMethod = "dictionaryService.update",
//        destroyMethod = "dictionaryService.destroy",
        paging = true,
        identifier = "uuid"
)
@JsonInclude(NON_NULL)
public class Dictionary extends AbstractPersistable {
    @NotBlank(message = "{fieldrequired}")
    @Indexed(unique = true)
    private String name;

    public Dictionary() {
    }
    
    public Dictionary(String id, String name) {
        super.setId(id);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName(String name) {
        return name;
    }
}
