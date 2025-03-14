package es.jclorenzo.exercises.springboot.bootstrap.test.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import es.jclorenzo.exercises.springboot.bootstrap.test.utils.PostgresTestContainer;

/**
 * The Class TestDatabaseConfig.
 */
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {
		"es.jclorenzo.exercises.springboot.infrastructure.config",
		"es.jclorenzo.exercises.springboot.application.config" })
public class ApiTestConfiguration {

	/**
	 * Data source.
	 *
	 * @return the data source
	 */
	@Bean
	DataSource dataSource() {
		final PostgresTestContainer container = PostgresTestContainer.getInstance();
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl(container.getJdbcUrl());
		dataSource.setUsername(container.getUsername());
		dataSource.setPassword(container.getPassword());
		return dataSource;
	}
}