package io.pivotal.microservices.services.words;

import io.pivotal.microservices.words.WordRepository;
import io.pivotal.microservices.words.WordsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.logging.Logger;
import static springfox.documentation.builders.PathSelectors.regex;
/**
 * Run as a micro-service, registering with the Discovery Server (Eureka).
 * <p>
 * Note that the configuration for this application is imported from
 * {@link WordsConfiguration}. This is a deliberate separation of concerns.
 * 
 * @author Andrew McDougall
 */
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableSwagger2
@Import(WordsConfiguration.class)
public class WordsServer {

	@Autowired
	protected WordRepository wordRepository;

	protected Logger logger = Logger.getLogger(WordsServer.class.getName());

	/**
	 * Run the application using Spring Boot and an embedded servlet engine.
	 * 
	 * @param args
	 *            Program arguments - ignored.
	 */
	public static void main(String[] args) {
		// Tell server to look for accounts-server.properties or
		// accounts-server.yml
		System.setProperty("spring.config.name", "words-server");

		SpringApplication.run(WordsServer.class, args);
	}

	/*
		This is how I figured out to add the Swagger
	 */
	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("hangman")
				.apiInfo(apiInfo())
				.select()
				.paths(regex("/api/v1/hangman.*"))
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Hangman Rest API with Swagger")
				.description("Hangman API")
				.termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
				.contact("Andrew McDougall")
				.license("Apache License Version 2.0")
				.licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
				.version("2.0")
				.build();
	}

}
