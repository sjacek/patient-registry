package com.grinno.patients.config;

import java.util.Collections;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.LocaleResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samskivert.mustache.Mustache;

import ch.ralscha.extdirectspring.util.JsonHandler;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "server")
class ServerProperties {

    private Integer port;
    private Integer httpPort;

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getHttpPort() {
        return this.httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }
}

@Configuration
@EnableConfigurationProperties(ServerProperties.class)
public class WebConfig {

    @Bean
    public ch.ralscha.extdirectspring.controller.Configuration configuration() {
        ch.ralscha.extdirectspring.controller.Configuration config = new ch.ralscha.extdirectspring.controller.Configuration();
        config.setExceptionToMessage(Collections.singletonMap(AccessDeniedException.class, "accessdenied"));
        return config;
    }

    @Bean
    public JsonHandler jsonHandler(ObjectMapper objectMapper) {
        JsonHandler jh = new JsonHandler();
        jh.setMapper(objectMapper);
        return jh;
    }

    @Bean
    public LocaleResolver localeResolver() {
        AppLocaleResolver resolver = new AppLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    @Bean
    public Mustache.Compiler mustacheCompiler() {
        return Mustache.compiler();
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    @Autowired
    ServerProperties serverProperties;

    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(serverProperties.getHttpPort());
        connector.setSecure(false);
        connector.setRedirectPort(serverProperties.getPort());

        return connector;
    }
}
