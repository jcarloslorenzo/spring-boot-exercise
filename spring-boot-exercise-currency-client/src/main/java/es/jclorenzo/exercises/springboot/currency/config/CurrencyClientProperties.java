package es.jclorenzo.exercises.springboot.currency.config;

import java.net.URI;

import lombok.Data;

/**
 * The Class CurrencyClientProperties.
 */
@Data
public class CurrencyClientProperties {

	/** The url. */
	private URI baseUrl;

	/** The username. */
	private String username;

	/** The password. */
	private String password;

}
