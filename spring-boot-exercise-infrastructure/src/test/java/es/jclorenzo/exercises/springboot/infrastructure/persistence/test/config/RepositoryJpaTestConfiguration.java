package es.jclorenzo.exercises.springboot.infrastructure.persistence.test.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import es.jclorenzo.exercises.springboot.infrastructure.config.PersistenceConfiguration;
import es.jclorenzo.exercises.springboot.infrastructure.persistence.test.utils.PostgresTestContainer;

/**
 * The Class RepositoryJapTestConfiguration.
 */
@Configuration
@Import(PersistenceConfiguration.class)
public class RepositoryJpaTestConfiguration {

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
