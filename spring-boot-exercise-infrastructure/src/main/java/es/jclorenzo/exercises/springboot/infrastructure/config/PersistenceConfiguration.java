package es.jclorenzo.exercises.springboot.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The Class ModuleConfiguration.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "es.jclorenzo.exercises.springboot.infrastructure.persistence" })
@EntityScan(basePackages = "es.jclorenzo.exercises.springboot.infrastructure.persistence.entity")
@EnableJpaRepositories(basePackages = "es.jclorenzo.exercises.springboot.infrastructure.persistence.repository")
public class PersistenceConfiguration {

}
