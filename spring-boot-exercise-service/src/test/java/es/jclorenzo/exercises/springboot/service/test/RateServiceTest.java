package es.jclorenzo.exercises.springboot.service.test;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClient;

import es.jclorenzo.exercises.springboot.currency.client.CurrencyRemoteClient;
import es.jclorenzo.exercises.springboot.currency.model.Currency;
import es.jclorenzo.exercises.springboot.repository.RateRepository;
import es.jclorenzo.exercises.springboot.repository.entity.RateEntity;
import es.jclorenzo.exercises.springboot.service.RateService;
import es.jclorenzo.exercises.springboot.service.config.ModuleConfiguration;
import es.jclorenzo.exercises.springboot.service.exception.DateRangeOverlapException;
import es.jclorenzo.exercises.springboot.service.model.RateVO;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class RateServiceTest.
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ModuleConfiguration.class)
public class RateServiceTest {

	/** The rest client. */
	@MockitoBean
	private RestClient restClient;

	/** The rate repo. */
	@MockitoBean
	private RateRepository rateRepo;

	/** The currency client. */
	@MockitoBean
	private CurrencyRemoteClient currencyClient;

	/** The rate service. */
	@Autowired
	private RateService rateService;

	/**
	 * Adds the new rate with invalid date range.
	 */
	@Test
	void addNewRate() {
		final int productId = 1;
		final int brandId = 1;
		final String currencyCode = "EUR";
		final LocalDate startDate = LocalDate.now();
		final LocalDate endDate = LocalDate.now().plusMonths(1);

		final String expectedRateFormattedPrice = "12.34 € (EUR)";

		Mockito.when(this.currencyClient.getCurrencyByCode(currencyCode))
				.thenReturn(new Currency("€", currencyCode, 2));

		Mockito.when(this.rateRepo.save(ArgumentMatchers.any(RateEntity.class)))
				.thenReturn(new RateEntity(1, brandId, productId, startDate, endDate, 1234, currencyCode));

		Assertions.assertDoesNotThrow(() -> {
			final RateVO rate = this.rateService.addNewRate(
					productId,
					brandId,
					startDate,
					endDate,
					12.34,
					currencyCode);
			Assertions.assertNotNull(rate);
			Assertions.assertEquals(expectedRateFormattedPrice, rate.formattedPrice());
		});
	}

	/**
	 * Adds the new with overlaped dates.
	 */
	@Test
	@SuppressWarnings("unchecked")
	void addNewWithOverlapedDates() {

		Mockito.when(this.rateRepo.exists(ArgumentMatchers.any(Specification.class)))
				.thenReturn(true);

		Assertions.assertThrows(DateRangeOverlapException.class, () -> {
			this.rateService.addNewRate(1, 1, LocalDate.now(), LocalDate.now(), 10.20, "EUR");

		});
	}

	/**
	 * Adds the new with unkown currency.
	 */
	@Test
	void addNewWithUnkownCurrency() {

		Mockito.when(this.currencyClient.getCurrencyByCode(ArgumentMatchers.any(String.class)))
				.thenThrow(new NoSuchElementException());

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.rateService.addNewRate(1, 1, LocalDate.now(), LocalDate.now(), 10.20, "XXX");
		});
	}

	/**
	 * Adds the new rate with invalid date range.
	 */
	@Test
	void addNewRateWithInvalidDateRange() {
		final int productId = 1;
		final int brandId = 1;
		final String currencyCode = "EUR";
		final LocalDate startDate = LocalDate.now();
		final LocalDate endDate = LocalDate.now().minusMonths(1);

		Mockito.when(this.currencyClient.getCurrencyByCode(currencyCode))
				.thenReturn(new Currency("€", currencyCode, 2));

		Assertions.assertEquals("Invalid date range",
				Assertions.assertThrows(IllegalArgumentException.class, () -> {
					this.rateService.addNewRate(
							productId,
							brandId,
							startDate,
							endDate,
							12.34,
							currencyCode);
				}).getMessage());
	}

	/**
	 * Search rate.
	 */
	@Test
	@SuppressWarnings("unchecked")
	void searchRate() {
		final Integer brandId = 1;
		final Integer productId = 2;
		final String currencyCode = "EUR";
		final LocalDate effectiveDate = LocalDate.now();

		final String expectedRateFormattedPrice = "12.34 € (EUR)";

		Mockito.when(this.currencyClient.getCurrencyByCode(currencyCode))
				.thenReturn(new Currency("€", currencyCode, 2));

		Mockito.when(this.rateRepo.findOne(ArgumentMatchers.any(Specification.class)))
				.thenReturn(
						Optional.of(
								new RateEntity(1, brandId, productId, LocalDate.now(), LocalDate.now(), 1234, "EUR")));

		Assertions.assertDoesNotThrow(() -> {
			final RateVO rate = this.rateService.search(productId, brandId, effectiveDate);
			Assertions.assertNotNull(rate);
			Assertions.assertEquals(expectedRateFormattedPrice, rate.formattedPrice());
		});

	}

	/**
	 * Search non existant rate.
	 */
	@Test
	@SuppressWarnings("unchecked")
	void searchNonExistentRate() {

		Mockito.when(
				this.rateRepo.findOne(ArgumentMatchers.any(Specification.class)))
				.thenReturn(Optional.empty());

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.rateService.search(1, 1, LocalDate.now());
		});

	}

	/**
	 * Find by id non existant rate.
	 */
	@Test
	void findByIdNonExistentRate() {
		final Integer rateId = 1;

		Mockito.when(this.rateRepo.findById(rateId)).thenReturn(Optional.empty());

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.rateService.findById(rateId);
		});

	}

	/**
	 * Find by id.
	 */
	@Test
	void findById() {
		final Integer rateId = 1;
		final String currencyCode = "EUR";

		final String expectedRateFormattedPrice = "12.34 € (EUR)";

		Mockito.when(this.currencyClient.getCurrencyByCode(currencyCode))
				.thenReturn(new Currency("€", currencyCode, 2));

		Mockito.when(this.rateRepo.findById(rateId))
				.thenReturn(Optional.of(new RateEntity(rateId, 1, 1, LocalDate.now(), LocalDate.now(), 1234, "EUR")));

		Assertions.assertDoesNotThrow(() -> {
			final RateVO rate = this.rateService.findById(rateId);
			Assertions.assertNotNull(rate);
			Assertions.assertEquals(expectedRateFormattedPrice, rate.formattedPrice());
		});

	}

	/**
	 * Update rate price.
	 */
	@Test
	void updateRatePrice() {

		final Integer rateId = 1;

		final String expectedRateFormattedPrice = "4.123 $ (USD)";

		Mockito.when(this.currencyClient.getCurrencyByCode("EUR"))
				.thenReturn(new Currency("€", "EUR", 2));

		Mockito.when(this.currencyClient.getCurrencyByCode("USD"))
				.thenReturn(new Currency("$", "USD", 3));

		Mockito.when(this.rateRepo.findById(rateId))
				.thenReturn(Optional.of(new RateEntity(rateId, 1, 1, LocalDate.now(), LocalDate.now(), 1234, "EUR")));

		Mockito.when(this.rateRepo.save(ArgumentMatchers.any(RateEntity.class)))
				.thenReturn(new RateEntity(rateId, 1, 1, LocalDate.now(), LocalDate.now(), 4123, "USD"));

		Assertions.assertDoesNotThrow(() -> {
			final RateVO rate = this.rateService.updatePrice(rateId, 4.123D, "USD");
			Assertions.assertNotNull(rate);
			Assertions.assertEquals(expectedRateFormattedPrice, rate.formattedPrice());
		});
	}

	/**
	 * Update non existent rate.
	 */
	@Test
	void updateNonExistentRate() {
		final Integer rateId = 1;

		Mockito.when(this.rateRepo.findById(rateId)).thenReturn(Optional.empty());

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.rateService.updatePrice(rateId, 10.20, "EUR");
		});
	}

	/**
	 * Delete rate.
	 */
	@Test
	void deleteRate() {
		final Integer rateId = 1;
		Assertions.assertDoesNotThrow(() -> {
			this.rateService.delete(rateId);
		});
	}

}
