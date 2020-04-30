package com.grinnotech.patients.config;

import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebFontMimeMapper implements
		WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
		mappings.add("eot", "application/vnd.ms-fontobject");
		mappings.add("woff", "application/font-woff");
		mappings.add("woff2", "application/font-woff2");
		mappings.add("ttf", "application/font-sfnt");
		mappings.add("otf", "application/font-sfnt");
		factory.setMimeMappings(mappings);

	}
}
