package es.jclorenzo.exercises.springboot.repository.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The Class ModuleConfiguration.
 */
@EnableTransactionManagement
@Configuration(value = "RepositoryModuleConfiguration")
@EntityScan(basePackages = "es.jclorenzo.exercises.springboot.repository.entity")
@EnableJpaRepositories(basePackages = "es.jclorenzo.exercises.springboot.repository")
public class ModuleConfiguration {

}
