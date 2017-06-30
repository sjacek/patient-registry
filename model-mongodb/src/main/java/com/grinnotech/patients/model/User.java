package com.grinnotech.patients.model;

import ch.rasc.extclassgenerator.Model;
import ch.rasc.extclassgenerator.ModelField;
import ch.rasc.extclassgenerator.ModelType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.grinnotech.patients.domain.AbstractPersistable;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Set;

@Document
@Model(value = "Patients.model.User",
        createMethod = "userService.update",
        readMethod = "userService.read",
        updateMethod = "userService.update",
        destroyMethod = "userService.destroy",
        rootProperty = "records",
        paging = true,
        identifier = "uuid")
@ModelField(value = "twoFactorAuth", persist = false, type = ModelType.BOOLEAN)
@JsonInclude(Include.NON_NULL)
public class User extends AbstractPersistable {

    @NotBlank(message = "{fieldrequired}")
    private String firstName;

    @NotBlank(message = "{fieldrequired}")
    private String lastName;

    @Email(message = "{invalidemail}")
    @NotBlank(message = "{fieldrequired}")
    private String email;

    private Set<String> organizationIds;

    @ModelField
    @ch.rasc.bsoncodec.annotation.Transient
//    @javax.persistence.Transient
    @org.springframework.data.annotation.Transient
    private Set<Organization> organizations;

    private Set<String> authorities;

    @JsonIgnore
    private String passwordHash;

    @NotBlank(message = "{fieldrequired}")
    private String locale;

    private boolean enabled;

    @ModelField(persist = false)
    private int failedLogins;

    @ModelField(dateFormat = "time", persist = false)
    private Date lockedOutUntil;

    @ModelField(dateFormat = "time", persist = false)
    private Date lastAccess;

    @JsonIgnore
    private String passwordResetToken;

    @JsonIgnore
    private Date passwordResetTokenValidUntil;

    @JsonIgnore
    private String secret;

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

    public Set<Organization> getOrganizations() {
        return this.organizations;
    }
    
    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }
    
    public Set<String> getOrganizationIds() {
        return this.organizationIds;
    }
    
    public void setOrganizationIds(Set<String> organizationIds) {
        this.organizationIds = organizationIds;
    }
    
    public Set<String> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public int getFailedLogins() {
        return this.failedLogins;
    }

    public void setFailedLogins(int failedLogins) {
        this.failedLogins = failedLogins;
    }

    public Date getLockedOutUntil() {
        return this.lockedOutUntil;
    }

    public void setLockedOutUntil(Date lockedOutUntil) {
        this.lockedOutUntil = lockedOutUntil;
    }

    public Date getLastAccess() {
        return this.lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getPasswordResetToken() {
        return this.passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public Date getPasswordResetTokenValidUntil() {
        return this.passwordResetTokenValidUntil;
    }

    public void setPasswordResetTokenValidUntil(Date passwordResetTokenValidUntil) {
        this.passwordResetTokenValidUntil = passwordResetTokenValidUntil;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isTwoFactorAuth() {
        return StringUtils.hasText(this.getSecret());
    }

}
