package es.jclorenzo.exercises.springboot.currency.client.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The Enum CurrencyEndpoints.
 */
@AllArgsConstructor
public enum CurrencyEndpoints {

	/** The currency. */
	CURRENCY("/v1/currencies/{currencyCode}"),

	/** The currencies. */
	CURRENCIES("/v1/currencies");

	/** The value. */
	@Getter
	private final String value;

}
