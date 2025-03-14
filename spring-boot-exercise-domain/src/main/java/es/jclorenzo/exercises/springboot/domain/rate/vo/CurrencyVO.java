package es.jclorenzo.exercises.springboot.domain.rate.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * The Class CurrencyVO.
 */
@AllArgsConstructor
@ToString(doNotUseGetters = true)
@Getter(value = AccessLevel.MODULE)
public class CurrencyVO {

	/** The symbol. */
	@Getter
	private final String symbol;

	/** The code. */
	@Getter
	private final String code;

	/** The decimals. */
	private final int decimals;

	/**
	 * Gets the format pattern.
	 *
	 * @return the format pattern
	 */
	protected String getCurrencyFormat() {
		return "%.".concat(String.valueOf(this.decimals)).concat("f %s (%s)");
	}

}
