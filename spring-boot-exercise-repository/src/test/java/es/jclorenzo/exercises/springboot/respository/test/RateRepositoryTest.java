package es.jclorenzo.exercises.springboot.respository.test;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import es.jclorenzo.exercises.springboot.repository.RateRepository;
import es.jclorenzo.exercises.springboot.repository.entity.RateEntity;
import es.jclorenzo.exercises.springboot.repository.specification.RateSearchByDateRangeSpecification;
import es.jclorenzo.exercises.springboot.repository.specification.RateSearchByEffectiveDateSpecification;
import es.jclorenzo.exercises.springboot.repository.specification.RateSearchByProductAndBrandSpecification;
import es.jclorenzo.exercises.springboot.respository.test.config.RepositoryTestConfiguration;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class RateRepositoryTest.
 */
@Slf4j
@SpringBootTest(classes = RepositoryTestConfiguration.class)
public class RateRepositoryTest {

	/** The rate repo. */
	@Autowired
	private RateRepository rateRepo;

	/**
	 * Test find by ID.
	 */
	@Test
	public void testFindByID() {
		final Integer rateId = 1;
		final Integer expectedPrize = 1550;

		Assertions.assertDoesNotThrow(() -> {
			final Optional<RateEntity> rate = this.rateRepo.findById(rateId);
			Assertions.assertTrue(rate.isPresent());
			Assertions.assertEquals(expectedPrize, rate.get().getPrice());
			RateRepositoryTest.log.debug("Rate -> {}", rate.get());
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

			final Optional<RateEntity> rate = this.rateRepo.findOne(
					Specification
							.where(RateSearchByProductAndBrandSpecification.getNewInstance(brandId, productId))
							.and(RateSearchByEffectiveDateSpecification.getNewInstance(effectiveDate)));

			Assertions.assertTrue(rate.isPresent());
			Assertions.assertEquals(expectedPrice, rate.get().getPrice());
			RateRepositoryTest.log.debug("Rate -> {}", rate.get());
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
					this.rateRepo.exists(
							Specification
									.where(RateSearchByProductAndBrandSpecification.getNewInstance(brandId, productId))
									.and(RateSearchByDateRangeSpecification.getNewInstance(startDate, endDate))));
		});
	}

}
