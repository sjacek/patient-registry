package com.grinnotech.patientsorig.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.grinnotech.patientsorig.config.security.MongoUserDetails;
import com.grinnotech.patients.model.Organization;
import com.grinnotech.patients.mongodb.model.User;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

import static com.grinnotech.patients.model.Authority.ADMIN;
import static com.grinnotech.patients.model.Authority.USER;
import static java.util.stream.Collectors.toMap;

@JsonInclude(Include.NON_NULL)
@Getter
@ToString
public class UserDetailDto {

    private final String firstName;

    private final String lastName;

    private final String locale;

    private final String autoOpenView;

    private final boolean screenLocked;

    private final boolean preAuth;

    private final Map<String,String> organizations;

    private final String csrf;

    public UserDetailDto(MongoUserDetails userDetails, User user, String csrf) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.locale = user.getLocale();
        this.screenLocked = userDetails.isScreenLocked();
        this.preAuth = userDetails.isPreAuth();

        if (userDetails.hasAuthority(ADMIN.name())) {
            this.autoOpenView = "users";
        } else if (userDetails.hasAuthority(USER.name())) {
            this.autoOpenView = "blank";
        } else {
            this.autoOpenView = null;
        }

        this.organizations = user.getOrganizations().stream().collect(toMap(Organization::getId, Organization::getCode));
        this.csrf = csrf;
    }
}
