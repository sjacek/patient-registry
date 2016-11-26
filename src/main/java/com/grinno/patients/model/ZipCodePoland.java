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

    public ZipCodePoland(String zipCode, String postOffice, String city, String voivodship, String street, String county) {
        this.zipCode = zipCode;
        this.postOffice = postOffice;
        this.city = city;
        this.voivodship = voivodship;
        this.street = street;
        this.county = county;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }
    
    public void setPostOffice(String postOffice) {
        this.postOffice = postOffice;
    }

    public String getPostOffice() {
        return postOffice;
    }
    
    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
    
    public void setVoivodship(String voivodship) {
        this.voivodship = voivodship;
    }

    public String getVoivodship() {
        return voivodship;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }
    
    public void setCounty(String county) {
        this.county = county;
    }

    public String getCounty() {
        return county;
    }
    
}
