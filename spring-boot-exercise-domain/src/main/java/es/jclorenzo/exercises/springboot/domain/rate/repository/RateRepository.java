package es.jclorenzo.exercises.springboot.domain.rate.repository;

import java.time.LocalDate;

import es.jclorenzo.exercises.springboot.domain.rate.Rate;

/**
 * The Interface RateRepository.
 */
public interface RateRepository {

	/**
	 * Save.
	 *
	 * @param rate the rate
	 * @return the rate
	 */
	Rate save(Rate rate);

	/**
	 * Delete.
	 *
	 * @param rateId the rate id
	 */
	void delete(Integer rateId);

	/**
	 * Find.
	 *
	 * @param rateId the rate id
	 * @return the rate
	 */
	Rate findById(Integer rateId);

	/**
	 * Search.
	 *
	 * @param brandId       the brand id
	 * @param productId     the product id
	 * @param effectiveDate the effective date
	 * @return the rate
	 */
	Rate search(Integer brandId, Integer productId, LocalDate effectiveDate);

	/**
	 * Exists.
	 *
	 * @param brandId            the brand id
	 * @param productId          the product id
	 * @param effectiveStartDate the effective start date
	 * @param effectiveEndDate   the effective end date
	 * @return true, if successful
	 */
	boolean exists(Integer brandId, Integer productId, LocalDate effectiveStartDate, LocalDate effectiveEndDate);
}
