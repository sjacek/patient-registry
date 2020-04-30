package com.grinnotech.patients.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "orphadata")
public class OrphadataProperties {

    private boolean enabled;

    private String urlPl;
}
