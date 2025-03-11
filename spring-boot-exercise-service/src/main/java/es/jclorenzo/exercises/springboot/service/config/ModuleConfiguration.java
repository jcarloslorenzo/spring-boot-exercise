package es.jclorenzo.exercises.springboot.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The Class ModuleConfiguration.
 */
@Configuration(value = "ServiceModuleConfiguration")
@ComponentScan(
		basePackages = { "es.jclorenzo.exercises.springboot.service", "es.jclorenzo.exercises.springboot.currency" })
public class ModuleConfiguration {

}
