package com.grinnotech.patients;

import com.grinnotech.patients.config.KeycloakProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

//@ComponentScan(basePackageClasses = {ExtDirectSpring.class, Application.class},
//        excludeFilters = {@ComponentScan.Filter(type = ASSIGNABLE_TYPE, value = ApiController.class)})
@ComponentScan
@EnableAutoConfiguration(exclude = {MustacheAutoConfiguration.class, SpringDataWebAutoConfiguration.class})
//@EnableAsync
//@EnableScheduling
//@EnableCaching
@Slf4j
@EnableConfigurationProperties(KeycloakProperties.class)
public class PatientsApp {

	public static void main(String[] args) {
        // -Dspring.profiles.active=development
//        if (AuthConfigFactory.getFactory() == null) {
//            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
//        }
		log.debug("main !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        SpringApplication.run(PatientsApp.class, args);
    }

//	@Bean
//	public WebMvcConfigurer webConfigurer() {
//		return new WebMvcConfigurer() {
//            /*@Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("*")
//                        .allowedMethods("*")
//                        .allowedHeaders("Content-Type");
//            }*/
//		};
//	}

//	@Bean
//	public Module jsonNullableModule() {
//		return new JsonNullableModule();
//	}
}
