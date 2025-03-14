package es.jclorenzo.exercises.springboot.infrastructure.config;

import java.net.URI;

import lombok.Data;

/**
 * The Class CurrencyExternalClientProperties.
 */
@Data
public class CurrencyExternalClientProperties {

	/** The url. */
	private URI baseUrl;

	/** The username. */
	private String username;

	/** The password. */
	private String password;

}
