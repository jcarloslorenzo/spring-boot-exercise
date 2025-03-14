package es.jclorenzo.exercises.springboot.domain.service;

import java.util.List;

import es.jclorenzo.exercises.springboot.domain.rate.vo.CurrencyVO;

/**
 * The Interface CurrencyService.
 */
public interface CurrencyService {

	/**
	 * Gets the currencies.
	 *
	 * @return the currencies
	 */
	List<CurrencyVO> getCurrencies();

	/**
	 * Find by code.
	 *
	 * @param currencyCode the currency code
	 * @return the currency
	 */
	CurrencyVO findByCode(String currencyCode);
}
