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

package com.grinno.patients.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import ch.rasc.extclassgenerator.ModelField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grinno.patients.model.User;
import java.io.Serializable;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author jacek
 */
@MappedSuperclass
public abstract class AbstractPersistable implements JsonItem, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ModelField(useNull = true, convert = "null")
    private String id;

    @Indexed
    @Field("_version")
    @JsonIgnore
    private Integer version;
    
    @Field("_created_by")
    @JsonIgnore
    private User createdBy;
    
    @Field("_created_date")
    @JsonIgnore
    @Temporal(DATE)
    private Date createdDate;
    
    @Indexed
    @Field("_deleted")
    @JsonIgnore
    private boolean deleted;
    
    @Field("_deleted_by")
    @JsonIgnore
    private User deletedBy;
    
    @Field("_deleted_date")
    @JsonIgnore
    @Temporal(DATE)
    private Date deletedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer incrementVersion() {
        if (version == null) version = 0;
        return ++version;
    }

    public User getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(User user) {
        this.createdBy = user;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public User getDeletedBy() {
        return deletedBy;
    }
    
    public void setDeletedBy(User user) {
        this.deletedBy = user;
    }
    
    public Date getDeletedDate() {
        return deletedDate;
    }
    
    public void setDeletedDate(Date date) {
        this.deletedDate = date;
    }
        
    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }

    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        AbstractPersistable that = (AbstractPersistable) obj;

        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? 0 : getId().hashCode() * 31;
        return hashCode;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        addJson(builder);
        return builder.build();
    }

    @Override
    public void addJson(JsonObjectBuilder builder) {
        builder.add("id", checkNull(id));
    }

    public static final String checkNull(String s) {
        if (s != null) return s;
        return "";
    }
    
    public static final Integer checkNull(Integer n) {
        if (n != null) return n;
        return -1;
    }
}
