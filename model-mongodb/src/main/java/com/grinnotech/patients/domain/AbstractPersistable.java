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

package com.grinnotech.patients.domain;

import ch.rasc.extclassgenerator.ModelField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grinnotech.patients.model.User;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;

/**
 *
 * @author Jacek Sztajnke
 */
@MappedSuperclass
public abstract class AbstractPersistable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ModelField(useNull = true, convert = "null")
    private String id;

    @Field("_version")
    private Integer version;

    @Indexed
    @Field("_active")
    @JsonIgnore
    private boolean active;
    
    @Field("_created_by")
    @JsonIgnore
    private User createdBy;
    
    @Field("_created_date")
    @JsonIgnore
    @Temporal(DATE)
    private Date createdDate;
    
    @Field("_updated_by")
    @JsonIgnore
    private User updatedBy;
    
    @Field("_updated_date")
    @JsonIgnore
    @Temporal(DATE)
    private Date updatedDate;
    
    @Field("_prev_id")
    @JsonIgnore
    private String prevId;
    
    @Indexed
    @Field("_chain_id")
    @JsonIgnore
    private String chainId;

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

    public String getChainId() {
        return chainId;
    }
    
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }
    
    public String getPrevId() {
        return prevId;
    }
    
    public void setPrevId(String prevId) {
        this.prevId = prevId;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
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
    
    public User getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }
    
    public Date getUpdatedDate() {
        return updatedDate;
    }
    
    public void setUpdatedDate(Date date) {
        this.updatedDate = date;
    }
    
    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

        return null != getId() && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return  17 + (null == getId() ? 0 : getId().hashCode() * 31);
    }

    public static String checkNull(String s) {
        if (s != null) return s;
        return "";
    }
    
    public static Integer checkNull(Integer n) {
        if (n != null) return n;
        return -1;
    }
}
