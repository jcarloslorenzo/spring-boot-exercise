package es.jclorenzo.exercises.springboot.infrastructure.api.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import es.jclorenzo.exercises.springboot.infrastructure.config.ApiConfiguration;

/**
 * The Class ControllerTestConfiguration.
 */
@Configuration
@Import(ApiConfiguration.class)
public class ControllerTestConfiguration {

}
