package com.grinnotech.patients.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="keycloak")
public class KeycloakProperties {

	public Boolean enabled;

	public String authServerUrl;

	public String realm;

	public String resource;

	public String requiredUserRole;

	public Credentials credentials;

	public static class Credentials {
		public String secret;
	}
}
