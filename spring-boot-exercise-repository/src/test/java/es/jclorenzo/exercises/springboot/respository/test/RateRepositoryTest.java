package es.jclorenzo.exercises.springboot.respository.test;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import es.jclorenzo.exercises.springboot.repository.RateRepository;
import es.jclorenzo.exercises.springboot.repository.config.ModuleConfiguration;
import es.jclorenzo.exercises.springboot.repository.entity.RateEntity;
import es.jclorenzo.exercises.springboot.repository.specification.RateSearchSpecification;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class RateRepositoryTest.
 */
@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ModuleConfiguration.class)
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
	public void specificationSearchTest() {
		final int brandId = 1;
		final int productId = 1;
		final LocalDate effectiveDate = LocalDate.of(2022, 6, 14);

		final Integer expectedPrice = 1850;

		Assertions.assertDoesNotThrow(() -> {

			final Optional<RateEntity> rate = this.rateRepo.findOne(
					RateSearchSpecification.getNewInstance(brandId, productId, effectiveDate));

			Assertions.assertTrue(rate.isPresent());
			Assertions.assertEquals(expectedPrice, rate.get().getPrice());
			RateRepositoryTest.log.debug("Rate -> {}", rate.get());
		});
	}

}
