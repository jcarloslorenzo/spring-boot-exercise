package es.jclorenzo.exercises.springboot.application.rate;

import java.time.LocalDate;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.jclorenzo.exercises.springboot.application.exception.DateRangeOverlapException;
import es.jclorenzo.exercises.springboot.domain.rate.Rate;
import es.jclorenzo.exercises.springboot.domain.rate.repository.RateRepository;
import es.jclorenzo.exercises.springboot.domain.service.CurrencyService;
import es.jclorenzo.exercises.springboot.domain.service.RateService;

/**
 * The Class RateServiceImpl.
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, readOnly = true)
public class RateServiceImpl implements RateService {

	/** The rate repo. */
	private final RateRepository rateRepository;

	/** The currency remote client. */
	private final CurrencyService currencyService;

	/**
	 * Instantiates a new rate service impl.
	 *
	 * @param rateRepository  the rate repository
	 * @param currencyService the currency service
	 */
	public RateServiceImpl(
			final RateRepository rateRepository,
			final CurrencyService currencyService) {

		this.rateRepository = rateRepository;
		this.currencyService = currencyService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Rate addNewRate(final Integer productId, final Integer brandId, final LocalDate startDate,
			final LocalDate endDate, final Double price, final String currencyCode) {

		if (this.rateRepository.exists(brandId, productId, startDate, endDate)) {

			throw new DateRangeOverlapException(
					"The effective date range of the new rate overlaps with an existing one.");
		}

		final Rate newRate = Rate.newInstance(
				productId, brandId,
				startDate, endDate,
				price, this.currencyService.findByCode(currencyCode));

		return this.rateRepository.save(newRate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Cacheable(value = "rates", key = "#productId + '-' + #brandId + '-' + #effectiveDate")
	public Rate search(final Integer productId, final Integer brandId, final LocalDate effectiveDate) {
		return this.rateRepository.search(brandId, productId, effectiveDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Cacheable(value = "rates", key = "#rateId")
	public Rate findById(final Integer rateId) {
		return this.rateRepository.findById(rateId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@CacheEvict(value = "rates", key = "#rateId")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public Rate updatePrice(final Integer rateId, final Double price, final String currencyCode) {

		return this.rateRepository.save(
				this.rateRepository.findById(rateId)
						.updatePrice(
								price,
								this.currencyService.findByCode(currencyCode)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@CacheEvict(value = "rates", key = "#rateId")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void delete(final Integer rateId) {
		this.rateRepository.delete(rateId);
	}

}
