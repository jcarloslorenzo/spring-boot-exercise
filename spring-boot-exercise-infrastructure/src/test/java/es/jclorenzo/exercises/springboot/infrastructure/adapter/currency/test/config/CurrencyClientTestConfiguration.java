package es.jclorenzo.exercises.springboot.infrastructure.adapter.currency.test.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import es.jclorenzo.exercises.springboot.infrastructure.config.AdapterConfiguration;

/**
 * The Class CurrencyClientTestConfiguration.
 */
@Configuration
@Import(AdapterConfiguration.class)
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "es.jclorenzo.exercises.springboot.infrastructure.adapter.currency")
public class CurrencyClientTestConfiguration {

}
