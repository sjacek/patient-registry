package com.grinnotech.patients.config;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.grinnotech.patients.view.CsvView;
//import com.grinnotech.patients.view.ExcelView;
//import com.grinnotech.patients.view.PdfView;
import com.samskivert.mustache.Mustache;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.apache.coyote.ajp.AjpNioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import java.util.Locale;

//import ch.ralscha.extdirectspring.util.JsonHandler;

@EnableWebMvc
@Configuration
@ComponentScan
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.defaultContentType(MediaType.APPLICATION_JSON).favorPathExtension(true);
	}

//	/*
//	 * Configure ContentNegotiatingViewResolver
//	 */
//	@Bean
//	public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
//		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
//		resolver.setContentNegotiationManager(manager);
//
//		// Define all possible view resolvers
//		List<ViewResolver> resolvers = new ArrayList<>();
//
//		//        resolvers.add(htmlViewResolver());
//		resolvers.add(csvViewResolver());
//		resolvers.add(excelViewResolver());
//		resolvers.add(pdfViewResolver());
//
//		resolver.setViewResolvers(resolvers);
//		return resolver;
//	}
//
//	//    private ViewResolver htmlViewResolver() {
//	//        return new ViewResolver() {
//	//            @Override
//	//            public View resolveViewName(String viewName, Locale locale) throws Exception {
//	//                InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//	//                return resolver;
//	//            }
//	//        };
//	//    }
//
//	/*
//	 * Configure View resolver to provide XLS output using Apache POI library to
//	 * generate XLS output for an object content
//	 */
//	@Bean
//	public ViewResolver excelViewResolver() {
//		return (s, locale) -> new ExcelView();
//	}
//
//	/*
//	 * Configure View resolver to provide Csv output using Super Csv library to
//	 * generate Csv output for an object content
//	 */
//	@Bean
//	public ViewResolver csvViewResolver() {
//		return (s, locale) -> new CsvView();
//	}
//
//	/*
//	 * Configure View resolver to provide Pdf output using iText library to
//	 * generate pdf output for an object content
//	 */
//	@Bean
//	public ViewResolver pdfViewResolver() {
//		return (s, locale) -> new PdfView();
//	}
//
//	//    -----------------------------------------------------------------------
//	@Bean
//	public ch.ralscha.extdirectspring.controller.Configuration configuration() {
//		ch.ralscha.extdirectspring.controller.Configuration config = new ch.ralscha.extdirectspring.controller.Configuration();
//		config.setExceptionToMessage(Collections.singletonMap(AccessDeniedException.class, "accessdenied"));
//		return config;
//	}

	//    @Bean
	//    public EmbeddedServletContainerCustomizer containerCustomizer() {
	//        return container -> container.setPort(8012);
	//    }
	//
//	@Bean
//	public JsonHandler jsonHandler(ObjectMapper objectMapper) {
//		JsonHandler jh = new JsonHandler();
//		jh.setMapper(objectMapper);
//		return jh;
//	}

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

	@Value("${tomcat.ajp.enabled:false}")
	private boolean tomcatAjpEnabled;

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(@NotNull Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};

		if (tomcatAjpEnabled) {
			tomcat.addAdditionalTomcatConnectors(initiateAjpConnector());
		}
		return tomcat;
	}

	@Value("${server.port}")
	private int port;

	@Value("${server.http-port}")
	private int httpPort;

	private @NotNull Connector initiateHttpConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(httpPort);
		connector.setSecure(false);
		connector.setRedirectPort(port);

		return connector;
	}

	@Value("${tomcat.ajp.port}")
	private int ajpPort;

	@Value("${tomcat.ajp.secret}")
	private String ajpSecret;

	@Value("${tomcat.ajp.secretRequired:true}")
	private Boolean ajpSecretRequired;

	private @NotNull Connector initiateAjpConnector() {
		Connector connector = new Connector("AJP/1.3");
		connector.setScheme("http");
		connector.setPort(ajpPort);
//		connector.setSecure(true);
		connector.setAllowTrace(false);
//		AbstractAjpProtocol<?> protocol = (AbstractAjpProtocol<?>) connector.getProtocolHandler();
//		protocol.setRequiredSecret(ajpSecret);

		return connector;
	}

}
