package es.jclorenzo.exercises.springboot.service.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The Class CurrencyVO.
 */
@AllArgsConstructor
@Getter(value = AccessLevel.PROTECTED)
public class CurrencyVO {

	/** The symbol. */
	private final String symbol;

	/** The code. */
	private final String code;

	/** The decimals. */
	private final int decimals;

}
