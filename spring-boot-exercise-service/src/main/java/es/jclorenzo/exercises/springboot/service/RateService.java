package es.jclorenzo.exercises.springboot.service;

import java.time.LocalDate;

import es.jclorenzo.exercises.springboot.service.model.RateVO;

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
	RateVO addNewRate(Integer productId, Integer brandId, LocalDate effectiveStartDate, LocalDate effectiveEndDate,
			Double price, String currencyCode);

	/**
	 * Search.
	 *
	 * @param productId     the product id
	 * @param brandId       the brand id
	 * @param effectiveDate the effective date
	 * @return the rate
	 */
	RateVO search(Integer productId, Integer brandId, LocalDate effectiveDate);

	/**
	 * Gets the by id.
	 *
	 * @param rateId the rate id
	 * @return the by id
	 */
	RateVO findById(Integer rateId);

	/**
	 * Update rate price.
	 *
	 * @param rateId the rate id
	 * @param price  the price
	 * @param currencyCode the currency code
	 * @return the rate
	 */
	RateVO updatePrice(Integer rateId, Double price, String currencyCode);

	/**
	 * Delete.
	 *
	 * @param rateId the rate id
	 */
	void delete(Integer rateId);

}
