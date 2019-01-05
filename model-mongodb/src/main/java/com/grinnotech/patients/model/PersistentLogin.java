package com.grinnotech.patients.model;

import ch.rasc.bsoncodec.annotation.Id;
import ch.rasc.bsoncodec.annotation.Transient;
import ch.rasc.extclassgenerator.Model;
import ch.rasc.extclassgenerator.ModelField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Document
@Model(value = "Patients.model.PersistentLogin",
        idProperty = "series",
        readMethod = "userConfigService.readPersistentLogins",
        destroyMethod = "userConfigService.destroyPersistentLogin",
        writeAllFields = false)
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@Builder
public class PersistentLogin {

    @Id
    private String series;

    private String userId;

    @JsonIgnore
    @NotNull
    private String token;

    @ModelField(dateFormat = "time")
    private Date lastUsed;

    @Size(max = 39)
    private String ipAddress;

    @JsonIgnore
    private String userAgent;

    @Transient
    private String userAgentName;

    @Transient
    private String userAgentVersion;

    @Transient
    private String operatingSystem;
}
