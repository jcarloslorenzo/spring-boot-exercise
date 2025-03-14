package es.jclorenzo.exercises.springboot.domain.rate.vo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * The Class PriceVO.
 */
@AllArgsConstructor
@ToString(doNotUseGetters = true)
public class PriceVO {

	/** The price. */
	private final Integer price;

	/** The currency. */
	private final CurrencyVO currency;

	/**
	 * Instantiates a new price VO.
	 *
	 * @param price    the price
	 * @param currency the currency
	 */
	public PriceVO(@NonNull final Double price, @NonNull final CurrencyVO currency) {

		this.price = BigDecimal.valueOf(price).multiply(BigDecimal.TEN.pow(currency.getDecimals())).intValue();
		this.currency = currency;
	}

	/**
	 * Gets the formatted as string.
	 *
	 * @return the formatted as string
	 */
	public String getFormattedAsString() {
		return String.format(
				this.currency.getCurrencyFormat(),
				BigDecimal.valueOf(this.price)
						.divide(BigDecimal.TEN.pow(this.currency.getDecimals())),
				this.currency.getSymbol(),
				this.currency.getCode());
	}

	/**
	 * Gets the currency code.
	 *
	 * @return the currency code
	 */
	public String getCurrencyCode() {
		return this.currency.getCode();
	}

	/**
	 * Gets the price as integer.
	 *
	 * @return the price as integer
	 */
	public Integer getPriceAsInteger() {
		return this.price;
	}

}
