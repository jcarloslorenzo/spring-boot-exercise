package es.jclorenzo.exercises.springboot.domain.rate;

import java.time.LocalDate;

import es.jclorenzo.exercises.springboot.domain.rate.vo.CurrencyVO;
import es.jclorenzo.exercises.springboot.domain.rate.vo.PriceVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * The Class RateVO.
 */
@AllArgsConstructor
@ToString(doNotUseGetters = true)
public class Rate {

	/** The id. */
	@Getter
	private final Integer id;

	/** The brand id. */
	@Getter
	@NonNull
	private final Integer brandId;

	/** The product id. */
	@Getter
	@NonNull
	private final Integer productId;

	/** The effective start date. */
	@Getter
	@NonNull
	private final LocalDate effectiveStartDate;

	/** The effective end date. */
	@Getter
	@NonNull
	private final LocalDate effectiveEndDate;

	/** The Price VO. */
	@NonNull
	private PriceVO price;

	/**
	 * Instantiates a new rate VO.
	 *
	 * @param brandId            the brand id
	 * @param productId          the product id
	 * @param effectiveStartDate the effective start date
	 * @param effectiveEndDate   the effective end date
	 * @param price              the price
	 * @param currency           the currency
	 * @return the rate VO
	 */
	public static Rate newInstance(final int brandId, final int productId,
			@NonNull final LocalDate effectiveStartDate,
			@NonNull final LocalDate effectiveEndDate,
			final double price, @NonNull final CurrencyVO currency) {

		Rate.validate(brandId, productId, effectiveStartDate, effectiveEndDate, price, currency);

		return new Rate(
				null,
				brandId,
				productId,
				effectiveStartDate,
				effectiveEndDate,
				new PriceVO(price, currency));

	}

	/**
	 * Validate.
	 *
	 * @param brandId            the brand id
	 * @param productId          the product id
	 * @param effectiveStartDate the effective start date
	 * @param effectiveEndDate   the effective end date
	 * @param price              the price
	 * @param currency           the currency
	 */
	private static void validate(final int brandId, final int productId,
			final LocalDate effectiveStartDate,
			final LocalDate effectiveEndDate,
			final double price, final CurrencyVO currency) {

		if (effectiveEndDate.isBefore(effectiveStartDate)) {
			throw new IllegalArgumentException("Invalid date range");
		}

	}

	/**
	 * Gets the currency code.
	 *
	 * @return the currency code
	 */
	public String getCurrencyCode() {
		return this.price.getCurrencyCode();
	}

	/**
	 * Gets the price.
	 *
	 * @return the price
	 */
	public Integer getPriceAsInteger() {
		return this.price.getPriceAsInteger();
	}

	/**
	 * Update price.
	 *
	 * @param newPrice the new price
	 * @param currency the currency
	 * @return the integer
	 */
	public Rate updatePrice(final Double newPrice, final CurrencyVO currency) {

		this.price = new PriceVO(newPrice, currency);
		return this;

	}

	/**
	 * Gets the formatted price.
	 *
	 * @return the formatted price
	 */
	public String formattedPrice() {
		return this.price.getFormattedAsString();
	}
}
