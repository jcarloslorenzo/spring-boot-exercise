package es.jclorenzo.exercises.springboot.domain.service;

import java.time.LocalDate;

import es.jclorenzo.exercises.springboot.domain.rate.Rate;

/**
 * The Interface RateService.
 */
public interface RateService {

	/**
	 * Adds the new.
	 *
	 * @param productId          the product id
	 * @param brandId            the brand id
	 * @param effectiveStartDate the effective start date
	 * @param effectiveEndDate   the effective end date
	 * @param price              the price
	 * @param currencyCode       the currency code
	 * @return the rate
	 */
	Rate addNewRate(Integer productId, Integer brandId, LocalDate effectiveStartDate, LocalDate effectiveEndDate,
			Double price, String currencyCode);

	/**
	 * Search.
	 *
	 * @param productId     the product id
	 * @param brandId       the brand id
	 * @param effectiveDate the effective date
	 * @return the rate
	 */
	Rate search(Integer productId, Integer brandId, LocalDate effectiveDate);

	/**
	 * Gets the by id.
	 *
	 * @param rateId the rate id
	 * @return the by id
	 */
	Rate findById(Integer rateId);

	/**
	 * Update rate price.
	 *
	 * @param rateId the rate id
	 * @param price  the price
	 * @param currencyCode the currency code
	 * @return the rate
	 */
	Rate updatePrice(Integer rateId, Double price, String currencyCode);

	/**
	 * Delete.
	 *
	 * @param rateId the rate id
	 */
	void delete(Integer rateId);

}
