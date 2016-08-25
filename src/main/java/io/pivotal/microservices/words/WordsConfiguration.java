package io.pivotal.microservices.words;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

/**
 * The accounts Spring configuration.
 * 
 * @author Andrew McDougall
 */
@Configuration
@ComponentScan("io.pivotal.microservices.words")
@EntityScan("io.pivotal.microservices.words")
@EnableJpaRepositories("io.pivotal.microservices.words")
@PropertySource("classpath:db-config.properties")
public class WordsConfiguration {

	protected Logger logger;

	public WordsConfiguration() {
		logger = Logger.getLogger(getClass().getName());
	}

	/**
	 * Creates an in-memory "Words based on Cartoon characters" database populated with test data for fast
	 * testing
	 */
	@Bean
	public DataSource dataSource() {
		logger.info("dataSource() invoked");

		// Create an in-memory H2 relational database containing some demo
		// words.
		DataSource dataSource = (new EmbeddedDatabaseBuilder()).addScript("classpath:testdb/schema.sql")
				.addScript("classpath:testdb/data.sql").setName("Hangman").build();

		logger.info("dataSource = " + dataSource);

		// Sanity check
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> words = jdbcTemplate.queryForList("SELECT word FROM T_WORD");
		logger.info("System has " + words.size() + " words");


		return dataSource;
	}
}
