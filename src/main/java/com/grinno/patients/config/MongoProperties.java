package com.grinno.patients.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mongodb")
public class MongoProperties {

    private String uri = "mongodb://localhost/patients?w=1&wtimeoutMS=0&journal=true";

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
