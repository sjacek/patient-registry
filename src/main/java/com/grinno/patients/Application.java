package com.grinno.patients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import ch.ralscha.extdirectspring.ExtDirectSpring;
import ch.ralscha.extdirectspring.controller.ApiController;
import javax.security.auth.message.config.AuthConfigFactory;
import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@Configuration
@ComponentScan(basePackageClasses = {ExtDirectSpring.class, Application.class},
        excludeFilters = {@ComponentScan.Filter(type = ASSIGNABLE_TYPE, value = ApiController.class)})
@EnableAutoConfiguration(exclude = {MustacheAutoConfiguration.class, SpringDataWebAutoConfiguration.class})
@EnableAsync
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        // -Dspring.profiles.active=development
        SpringApplication.run(Application.class, args);
    }

}
