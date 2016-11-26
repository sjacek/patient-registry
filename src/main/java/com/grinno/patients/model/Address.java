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
import ch.rasc.extclassgenerator.ModelField;

/**
 *
 * @author Jacek Sztajnke
 */
@Model(value = "Patients.model.Address")
public class Address {
    
    private String street;
    
    private String house;
    
    private String flat;
    
//    @ModelField(customType = "zipcode")
    private String zipCode;
    
    private String city;
    
    private String postOffice;
    
    private String county;

    private String voivodship;
    
    @ModelField(defaultValue = "Polska")
    private String country;

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

    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
}
