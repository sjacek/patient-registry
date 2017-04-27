package com.grinnotech.patients.config;

import ch.ralscha.extdirectspring.util.JsonHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samskivert.mustache.Mustache;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Collections;
import java.util.Locale;

@Configuration
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

//    @Value("${tomcat.ajp.remoteauthentication}")
//    String remoteAuthentication;

    @Value("${tomcat.ajp.enabled}")
    boolean tomcatAjpEnabled;

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
        if (tomcatAjpEnabled) {
            tomcat.addAdditionalTomcatConnectors(initiateAjpConnector());
        }

        return tomcat;
    }

    @Value("${server.port}")
    private int port;

    @Value("${server.httpPort}")
    private int httpPort;

    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(port);

        return connector;
    }

    @Value("${tomcat.ajp.port}")
    int ajpPort;

    private Connector initiateAjpConnector() {
        Connector connector = new Connector("AJP/1.3");
//        connector.setProtocol("AJP/1.3");
        connector.setScheme("http");
        connector.setPort(ajpPort);
        connector.setSecure(false);
        connector.setAllowTrace(false);

        return connector;
    }

}
