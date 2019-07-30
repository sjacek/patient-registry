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

import ch.rasc.extclassgenerator.Model;
import ch.rasc.extclassgenerator.ModelField;
import javax.validation.constraints.NotBlank;
//import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 *
 * @author Jacek Sztajnke
 */
@Model(value = "Patients.model.AddressPl")
public class AddressPl extends Address {
    
    @Indexed
    @ModelField(defaultValue = "Pl")
    @NotBlank(message = "{fieldrequired}")
    private String country;

    private String street;
    
    private String house;
    
    private String flat;
    
    private String postOffice;
    
    @Indexed
    private String county;

    @Indexed
    private String voivodship;
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getHouse() {
        return house;
    }
    
    public void setHouse(String house) {
        this.house = house;
    }
    
    public String getFlat() {
        return flat;
    }
    
    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getPostOffice() {
        return postOffice;
    }
    
    public void setPostOffice(String postOffice) {
        this.postOffice = postOffice;
    }

    public String getCounty() {
        return county;
    }
    
    public void setCounty(String county) {
        this.county = county;
    }

    public String getVoivodship() {
        return voivodship;
    }
    
    public void setVoivodship(String voivodship) {
        this.voivodship = voivodship;
    }
}
