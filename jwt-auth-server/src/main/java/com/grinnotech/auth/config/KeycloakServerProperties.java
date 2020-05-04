package com.grinnotech.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "keycloak.server")
@Getter
@Setter
public class KeycloakServerProperties {

    String contextPath = "/auth";

    String realmImportFile = "keycloak-realm.json";

    AdminUser adminUser = new AdminUser();

    @Getter
    @Setter
    public static class AdminUser {

        String username = "admin";

        String password = "admin";
    }
}
