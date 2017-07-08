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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 *
 * @author Jacek Sztajnke
 */
@Document(collection="dic_zipcode_pl")
@Model(value = "Patients.model.ZipCodePoland",
        createMethod = "zipCodePolandService.update",
        readMethod = "zipCodePolandService.read",
        updateMethod = "zipCodePolandService.update",
        destroyMethod = "zipCodePolandService.destroy",
        paging = true,
        identifier = "uuid")
@JsonInclude(NON_NULL)
@Builder
@Getter
@Setter
public class ZipCodePoland extends AbstractPersistable {

    @Indexed
    @NotBlank(message = "{fieldrequired}")    
    private String zipCode;
    
    private String postOffice;
    
    @Indexed
    @NotBlank(message = "{fieldrequired}")    
    private String city;
    
    @Indexed
    @NotBlank(message = "{fieldrequired}")    
    private String voivodship;
    
    private String street;
    
    @Indexed
    private String county;
}
