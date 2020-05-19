package com.grinnotech.patientsorig;

import com.fasterxml.jackson.databind.Module;

import org.jetbrains.annotations.NotNull;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

//@ComponentScan(basePackageClasses = {ExtDirectSpring.class, Application.class},
//        excludeFilters = {@ComponentScan.Filter(type = ASSIGNABLE_TYPE, value = ApiController.class)})
@ComponentScan
@EnableAutoConfiguration(exclude = {MustacheAutoConfiguration.class, SpringDataWebAutoConfiguration.class})
//@EnableAsync
//@EnableScheduling
//@EnableCaching
@Slf4j
public class Application {

	public static void main(String[] args) {
        // -Dspring.profiles.active=development
//        if (AuthConfigFactory.getFactory() == null) {
//            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
//        }
		log.debug("main !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        SpringApplication.run(Application.class, args);
    }

	@Bean
	public WebMvcConfigurer webConfigurer() {
		return new WebMvcConfigurer() {
            /*@Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("Content-Type");
            }*/
		};
	}

	@Bean
	public Module jsonNullableModule() {
		return new JsonNullableModule();
	}
}
