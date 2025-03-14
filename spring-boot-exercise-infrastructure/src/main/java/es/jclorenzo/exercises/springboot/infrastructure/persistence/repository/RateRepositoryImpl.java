package es.jclorenzo.exercises.springboot.infrastructure.persistence.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import es.jclorenzo.exercises.springboot.domain.rate.Rate;
import es.jclorenzo.exercises.springboot.domain.rate.repository.RateRepository;
import es.jclorenzo.exercises.springboot.infrastructure.persistence.entity.mapper.RateEntityMapper;
import es.jclorenzo.exercises.springboot.infrastructure.persistence.repository.specification.RateSearchByDateRangeSpecification;
import es.jclorenzo.exercises.springboot.infrastructure.persistence.repository.specification.RateSearchByEffectiveDateSpecification;
import es.jclorenzo.exercises.springboot.infrastructure.persistence.repository.specification.RateSearchByProductAndBrandSpecification;

/**
 * The Class RateRepositoryImpl.
 */
@Repository
public class RateRepositoryImpl implements RateRepository {

	/** The jpa repo. */
	private final RateJpaRepository jpaRepo;

	/** The entity mapper. */
	private final RateEntityMapper entityMapper;

	/**
	 * Instantiates a new rate repository impl.
	 *
	 * @param jpaRepo      the jpa repo
	 * @param entityMapper the entity mapper
	 */
	public RateRepositoryImpl(
			final RateJpaRepository jpaRepo ,
			final RateEntityMapper entityMapper) {
		this.jpaRepo = jpaRepo;
		this.entityMapper = entityMapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rate save(final Rate rate) {
		return this.entityMapper.toDomainEntity(
				this.jpaRepo.save(
						this.entityMapper.fromDomainEntity(rate)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final Integer rateId) {
		this.jpaRepo.deleteById(rateId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rate findById(final Integer rateId) {
		return this.entityMapper.toDomainEntity(
				this.jpaRepo.findById(rateId).orElseThrow());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rate search(final Integer brandId, final Integer productId, final LocalDate effectiveDate) {
		return this.entityMapper.toDomainEntity(
				this.jpaRepo.findOne(
						Specification
								.where(RateSearchByProductAndBrandSpecification.getNewInstance(brandId, productId))
								.and(RateSearchByEffectiveDateSpecification.getNewInstance(effectiveDate)))
						.orElseThrow());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists(final Integer brandId, final Integer productId, final LocalDate effectiveStartDate,
			final LocalDate effectiveEndDate) {
		return this.jpaRepo.exists(
				Specification
						.where(RateSearchByProductAndBrandSpecification.getNewInstance(brandId, productId))
						.and(RateSearchByDateRangeSpecification.getNewInstance(effectiveStartDate, effectiveEndDate)));
	}

}
