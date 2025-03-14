package es.jclorenzo.exercises.springboot.infrastructure.persistence.test;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import es.jclorenzo.exercises.springboot.domain.service.CurrencyService;
import es.jclorenzo.exercises.springboot.infrastructure.persistence.entity.RateEntity;
import es.jclorenzo.exercises.springboot.infrastructure.persistence.repository.RateJpaRepository;
import es.jclorenzo.exercises.springboot.infrastructure.persistence.repository.specification.RateSearchByDateRangeSpecification;
import es.jclorenzo.exercises.springboot.infrastructure.persistence.repository.specification.RateSearchByEffectiveDateSpecification;
import es.jclorenzo.exercises.springboot.infrastructure.persistence.repository.specification.RateSearchByProductAndBrandSpecification;
import es.jclorenzo.exercises.springboot.infrastructure.persistence.test.config.RepositoryJpaTestConfiguration;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class RateRepositoryTest.
 */
@Slf4j
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = RepositoryJpaTestConfiguration.class)
public class RateJpaRepositoryTest {

	/** The currency service. */
	@MockitoBean
	private CurrencyService currencyService;

	/** The rate repo. */
	@Autowired
	private RateJpaRepository rateJpaRepo;

	/**
	 * Test find by ID.
	 */
	@Test
	public void testFindByID() {
		final Integer rateId = 1;
		final Integer expectedPrize = 1550;

		Assertions.assertDoesNotThrow(() -> {
			final Optional<RateEntity> optionalRate = this.rateJpaRepo.findById(rateId);
			Assertions.assertTrue(optionalRate.isPresent());
			Assertions.assertEquals(expectedPrize, optionalRate.get().getPrice());
			RateJpaRepositoryTest.log.debug("Rate -> {}", optionalRate.get());
		});
	}

	/**
	 * Specification search test.
	 */
	@Test
	public void specificationSearchByEffectiveDateTest() {
		final int brandId = 1;
		final int productId = 1;
		final LocalDate effectiveDate = LocalDate.of(2022, 6, 14);

		final Integer expectedPrice = 1850;

		Assertions.assertDoesNotThrow(() -> {

			final Optional<RateEntity> rate = this.rateJpaRepo.findOne(
					Specification
							.where(RateSearchByProductAndBrandSpecification.getNewInstance(brandId, productId))
							.and(RateSearchByEffectiveDateSpecification.getNewInstance(effectiveDate)));

			Assertions.assertTrue(rate.isPresent());
			Assertions.assertEquals(expectedPrice, rate.get().getPrice());
			RateJpaRepositoryTest.log.debug("Rate -> {}", rate.get());
		});
	}

	/**
	 * Specification search test.
	 */
	@Test
	public void specificationExistByDateRangeTest() {
		final int brandId = 1;
		final int productId = 1;
		final LocalDate endDate = LocalDate.of(2022, 6, 14);
		final LocalDate startDate = LocalDate.of(2022, 1, 1);

		Assertions.assertDoesNotThrow(() -> {

			Assertions.assertTrue(
					this.rateJpaRepo.exists(
							Specification
									.where(RateSearchByProductAndBrandSpecification.getNewInstance(brandId, productId))
									.and(RateSearchByDateRangeSpecification.getNewInstance(startDate, endDate))));
		});
	}

}
