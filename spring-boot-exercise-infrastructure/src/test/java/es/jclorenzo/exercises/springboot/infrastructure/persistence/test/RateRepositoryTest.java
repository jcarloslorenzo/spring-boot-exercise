package es.jclorenzo.exercises.springboot.infrastructure.persistence.test;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import es.jclorenzo.exercises.springboot.domain.rate.Rate;
import es.jclorenzo.exercises.springboot.domain.rate.repository.RateRepository;
import es.jclorenzo.exercises.springboot.domain.rate.vo.CurrencyVO;
import es.jclorenzo.exercises.springboot.domain.service.CurrencyService;
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
public class RateRepositoryTest {

	/** The currency service. */
	@MockitoBean
	private CurrencyService currencyService;

	/** The rate repository. */
	@Autowired
	private RateRepository rateRepo;

	/**
	 * Test find by ID.
	 */
	@BeforeEach
	void beforeEach() {
		Mockito.when(this.currencyService.findByCode("EUR")).thenReturn(this.getEuroCurrency());
		Mockito.when(this.currencyService.findByCode("USD")).thenReturn(this.getDollarCurrency());
	}

	/**
	 * Save.
	 */
	@Test
	public void save() {

		final String expectedPrize = "45,21 $ (USD)";
		Assertions.assertDoesNotThrow(() -> {
			final Rate rate = this.rateRepo.save(this.getRate());
			Assertions.assertNotNull(rate);
			Assertions.assertEquals(expectedPrize, rate.formattedPrice());
			RateRepositoryTest.log.info("Rate -> {}", rate);
		});
	}

	/**
	 * Test find by ID.
	 */
	@Test
	public void testFindByID() {
		final Integer rateId = 1;
		final String expectedPrize = "15,50 € (EUR)";

		Assertions.assertDoesNotThrow(() -> {
			final Rate rate = this.rateRepo.findById(rateId);
			Assertions.assertNotNull(rate);
			Assertions.assertEquals(expectedPrize, rate.formattedPrice());
			RateRepositoryTest.log.info("Rate -> {}", rate);
		});
	}

	/**
	 * Test find by ID.
	 */
	@Test
	public void search() {

		final int brandId = 1;
		final int productId = 1;
		final LocalDate effectiveDate = LocalDate.of(2022, 03, 14);

		final String expectedPrize = "15,50 € (EUR)";

		Assertions.assertDoesNotThrow(() -> {
			final Rate rate = this.rateRepo.search(brandId, productId, effectiveDate);
			Assertions.assertNotNull(rate);
			Assertions.assertEquals(expectedPrize, rate.formattedPrice());
			RateRepositoryTest.log.info("Rate -> {}", rate);
		});
	}

	/**
	 * Exists.
	 */
	@Test
	public void exists() {

		final int brandId = 1;
		final int productId = 1;
		final LocalDate startDate = LocalDate.of(2022, 03, 14);
		final LocalDate endDate = LocalDate.of(2022, 03, 14);

		Assertions.assertDoesNotThrow(() -> {
			Assertions.assertTrue(this.rateRepo.exists(brandId, productId, startDate, endDate));
		});
	}

	/**
	 * Delete.
	 */
	@Test
	public void delete() {
		final Integer rateId = 6;

		Assertions.assertDoesNotThrow(() -> {
			this.rateRepo.delete(rateId);
		});
	}

	/**
	 * Gets the euro currency.
	 *
	 * @return the euro currency
	 */
	private CurrencyVO getEuroCurrency() {
		return new CurrencyVO("€", "EUR", 2);
	}

	/**
	 * Ge dollar currency.
	 *
	 * @return the currency VO
	 */
	private CurrencyVO getDollarCurrency() {
		return new CurrencyVO("$", "USD", 2);
	}

	/**
	 * Gets the defaul rate.
	 *
	 * @return the defaul rate
	 */
	private Rate getRate() {
		final int brandId = 9;
		final int productId = 9999;
		final LocalDate startDate = LocalDate.now();
		final LocalDate endDate = LocalDate.now().plusMonths(1);
		final double price = 45.21D;

		return Rate.newInstance(brandId, productId, startDate, endDate, price, this.getDollarCurrency());
	}
}
