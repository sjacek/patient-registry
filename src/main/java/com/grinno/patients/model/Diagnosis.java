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
import java.util.List;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author Jacek Sztajnke
 */
//@Document(collection="dictionary")
@Document
@Model(value = "Patients.model.Diagnosis",
        createMethod = "diagnosisService.update",
        readMethod = "diagnosisService.read",
        updateMethod = "diagnosisService.update",
        destroyMethod = "diagnosisService.destroy",
        paging = true,
        identifier = "uuid")
@JsonInclude(NON_NULL)
public class Diagnosis extends Dictionary {

    @NotBlank(message = "{fieldrequired}")
    @Indexed
    private String diagnosisName;

    private List<String> otherNames;

    @NotBlank(message = "{fieldrequired}")
    private String diagnosisEnglishName;
    
    @NotBlank(message = "{fieldrequired}")
    private String icd10;

    private String description;
    
    public String getDiagnosisName() {
        return diagnosisName;
    }
    
    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    public List<String> getOtherNames() {
        return otherNames;
    }
    
    public void setOtherNames(List<String> otherName) {
        this.otherNames = otherName;
    }

    public String getDiagnosisEnglishName() {
        return diagnosisEnglishName;
    }
    
    public void setDiagnosisEnglishName(String diagnosisEnglishName) {
        this.diagnosisEnglishName = diagnosisEnglishName;
    }

    public String getIcd10() {
        return icd10;
    }
    
    public void setIcd10(String icd10) {
        this.icd10 = icd10;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return getId() + "[" + getDiagnosisName() + "]";
    }
}
