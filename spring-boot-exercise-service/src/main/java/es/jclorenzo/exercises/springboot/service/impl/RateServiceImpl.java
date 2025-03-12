package es.jclorenzo.exercises.springboot.service.impl;

import java.time.LocalDate;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.jclorenzo.exercises.springboot.currency.client.CurrencyRemoteClient;
import es.jclorenzo.exercises.springboot.repository.RateRepository;
import es.jclorenzo.exercises.springboot.repository.specification.RateSearchByDateRangeSpecification;
import es.jclorenzo.exercises.springboot.repository.specification.RateSearchByEffectiveDateSpecification;
import es.jclorenzo.exercises.springboot.repository.specification.RateSearchByProductAndBrandSpecification;
import es.jclorenzo.exercises.springboot.service.RateService;
import es.jclorenzo.exercises.springboot.service.exception.DateRangeOverlapException;
import es.jclorenzo.exercises.springboot.service.model.RateVO;
import es.jclorenzo.exercises.springboot.service.model.mapper.CurrencyVOMapper;
import es.jclorenzo.exercises.springboot.service.model.mapper.RateVOMapper;

/**
 * The Class RateServiceImpl.
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, readOnly = true)
public class RateServiceImpl implements RateService {

	/** The rate mapper. */
	private final RateVOMapper rateVOMapper;

	/** The rate repo. */
	private final RateRepository rateRepo;

	/** The currency mapper. */
	private final CurrencyVOMapper currencyMapper;

	/** The currency remote client. */
	private final CurrencyRemoteClient currencyRemoteClient;

	/**
	 * Instantiates a new rate service impl.
	 *
	 * @param rateMapper           the rate mapper
	 * @param rateRepo             the rate repo
	 * @param currencyMapper       the currency mapper
	 * @param currencyRemoteClient the currency remote client
	 */
	public RateServiceImpl(
			final RateVOMapper rateMapper ,
			final RateRepository rateRepo ,
			final CurrencyVOMapper currencyMapper ,
			final CurrencyRemoteClient currencyRemoteClient) {

		this.rateRepo = rateRepo;
		this.rateVOMapper = rateMapper;
		this.currencyMapper = currencyMapper;
		this.currencyRemoteClient = currencyRemoteClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public RateVO addNewRate(final Integer productId, final Integer brandId, final LocalDate startDate,
			final LocalDate endDate, final Double price, final String currencyCode) {

		if (this.rateRepo.exists(
				Specification
						.where(RateSearchByProductAndBrandSpecification.getNewInstance(brandId, productId))
						.and(RateSearchByDateRangeSpecification.getNewInstance(startDate, endDate)))) {

			throw new DateRangeOverlapException(
					"The effective date range of the new rate overlaps with an existing one.");
		}

		final RateVO newRate = RateVO.newInstance(
				productId,
				brandId,
				startDate,
				endDate,
				price, this.currencyMapper.fromRecord(this.currencyRemoteClient.getCurrencyByCode(currencyCode)));

		return this.rateVOMapper.fromEntity(
				this.rateRepo.save(
						this.rateVOMapper.toEntity(newRate)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Cacheable(value = "rates", key = "#productId + '-' + #brandId + '-' + #effectiveDate")
	public RateVO search(final Integer productId, final Integer brandId, final LocalDate effectiveDate) {
		return this.rateVOMapper.fromEntity(
				this.rateRepo.findOne(
						Specification
								.where(RateSearchByProductAndBrandSpecification.getNewInstance(brandId, productId))
								.and(RateSearchByEffectiveDateSpecification.getNewInstance(effectiveDate)))
						.orElseThrow());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Cacheable(value = "rates", key = "#rateId")
	public RateVO findById(final Integer rateId) {
		return this.rateVOMapper.fromEntity(
				this.rateRepo.findById(rateId)
						.orElseThrow());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@CacheEvict(value = "rates", key = "#rateId")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public RateVO updatePrice(final Integer rateId, final Double price, final String currencyCode) {

		final RateVO rate = this.rateVOMapper.fromEntity(this.rateRepo.findById(rateId).orElseThrow());

		rate.updatePrice(
				price,
				this.currencyMapper.fromRecord(this.currencyRemoteClient.getCurrencyByCode(currencyCode)));

		return this.rateVOMapper.fromEntity(
				this.rateRepo.save(this.rateVOMapper.toEntity(rate)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@CacheEvict(value = "rates", key = "#rateId")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void delete(final Integer rateId) {
		this.rateRepo.deleteById(rateId);
	}

}
