package com.grinnotech.patients;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import com.fasterxml.jackson.databind.Module;

import org.openapitools.jackson.nullable.JsonNullableModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.lang.invoke.MethodHandles;

import ch.ralscha.extdirectspring.ExtDirectSpring;
import ch.ralscha.extdirectspring.controller.ApiController;

@ComponentScan(basePackageClasses = {ExtDirectSpring.class, Application.class},
        excludeFilters = {@ComponentScan.Filter(type = ASSIGNABLE_TYPE, value = ApiController.class)})
@EnableAutoConfiguration(exclude = {MustacheAutoConfiguration.class, SpringDataWebAutoConfiguration.class})
@EnableAsync
@EnableScheduling
@EnableCaching
public class Application implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	static class ExitException extends RuntimeException implements ExitCodeGenerator {
		private static final long serialVersionUID = 1L;

		@Override
		public int getExitCode() {
			return 10;
		}

	}

	public static void main(String[] args) {
        // -Dspring.profiles.active=development
//        if (AuthConfigFactory.getFactory() == null) {
//            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
//        }
		logger.debug("main !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        SpringApplication.run(Application.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		logger.debug("run !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		if (args.length > 0 && args[0].equals("exitcode")) {
			throw new ExitException();
		}
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
