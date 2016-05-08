/*
 * Copyright (C) 2016 jacek
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import com.grinno.patients.domain.AbstractEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

/**
 *
 * @author jacek
 */
@Document
@JsonInclude(NON_NULL)
public class User extends AbstractEntity {

    private static Logger LOGGER = LoggerFactory.getLogger(User.class);

    @Id
    private String id;

    @NotBlank(message = "{fieldrequired}")
    private String loginName;

    @NotBlank(message = "{fieldrequired}")
    private String lastName;

    @NotBlank(message = "{fieldrequired}")
    private String firstName;

    @Email(message = "{invalidemail}")
    @NotBlank(message = "{fieldrequired}")
    private String email;

//    private List<String> authorities;
//
//    @JsonIgnore
//    private String passwordHash;
//
//    @NotBlank(message = "{fieldrequired}")
//    private String locale;
//
//    private boolean enabled;
//
//    @JsonIgnore
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
//    private Set<PersistentLogin> persistentLogins = new HashSet<>();
//
//    @Transient
//    private int failedLogins;
//
//    @Transient
//    private Date lockedOutUntil;
//
//    @Transient
//    private Date lastAccess;
//
//    @JsonIgnore
//    private String passwordResetToken;
//
//    @JsonIgnore
//    private Date passwordResetTokenValidUntil;
//
//    @JsonIgnore
//    private boolean deleted;
//
//    @JsonIgnore
//    private String secret;
//
    public User() {
        LOGGER.debug("User()");
    }

    public User(String loginName, String firstName, String lastName, String email) {
        LOGGER.debug("User(" + loginName +"," + firstName + "," + lastName + "," + email + ")");
        this.loginName = loginName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public List<String> getAuthorities() {
//        return this.authorities;
//    }
//
//    public void setAuthorities(List<String> authorities) {
//        this.authorities = authorities;
//    }
//
//    public String getPasswordHash() {
//        return this.passwordHash;
//    }
//
//    public void setPasswordHash(String passwordHash) {
//        this.passwordHash = passwordHash;
//    }
//
//    public boolean isEnabled() {
//        return this.enabled;
//    }
//
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//    }
//
//    public String getLocale() {
//        return this.locale;
//    }
//
//    public void setLocale(String locale) {
//        this.locale = locale;
//    }
//
//    public int getFailedLogins() {
//        return this.failedLogins;
//    }
//
//    public void setFailedLogins(int failedLogins) {
//        this.failedLogins = failedLogins;
//    }
//
//    public Date getLockedOutUntil() {
//        return this.lockedOutUntil;
//    }
//
//    public void setLockedOutUntil(Date lockedOutUntil) {
//        this.lockedOutUntil = lockedOutUntil;
//    }
//
//    public Date getLastAccess() {
//        return this.lastAccess;
//    }
//
//    public void setLastAccess(Date lastAccess) {
//        this.lastAccess = lastAccess;
//    }
//
//    public String getPasswordResetToken() {
//        return this.passwordResetToken;
//    }
//
//    public void setPasswordResetToken(String passwordResetToken) {
//        this.passwordResetToken = passwordResetToken;
//    }
//
//    public Date getPasswordResetTokenValidUntil() {
//        return this.passwordResetTokenValidUntil;
//    }
//
//    public void setPasswordResetTokenValidUntil(Date passwordResetTokenValidUntil) {
//        this.passwordResetTokenValidUntil = passwordResetTokenValidUntil;
//    }
//
//    public boolean isDeleted() {
//        return this.deleted;
//    }
//
//    public void setDeleted(boolean deleted) {
//        this.deleted = deleted;
//    }
//
//    public Set<PersistentLogin> getPersistentLogins() {
//	return this.persistentLogins;
//    }
//
//    public void setPersistentLogins(Set<PersistentLogin> persistentLogins) {
//	this.persistentLogins = persistentLogins;
//    }
//
//    public String getSecret() {
//        return this.secret;
//    }
//
//    public void setSecret(String secret) {
//        this.secret = secret;
//    }
//
//    public boolean isTwoFactorAuth() {
//        return StringUtils.hasText(this.getSecret());
//    }

    @Override
    public String toString() {
        return super.toString() + "[" + getLoginName() + ", " + getFirstName() + ", " + getLastName() + "]";
    }

    @Override
    public void addJson(JsonObjectBuilder builder) {

//        final JsonArrayBuilder authoritiesBuilder = Json.createArrayBuilder();
//        if (authorities != null)
//            authorities.forEach((s) -> authoritiesBuilder.add(s));
//
        builder.add("id", id)
                .add("loginName", loginName)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("email", email)
//                .add("authorities", authoritiesBuilder)
//                .add("locale", locale)
//                .add("enabled", enabled)
                ;
    }
}
