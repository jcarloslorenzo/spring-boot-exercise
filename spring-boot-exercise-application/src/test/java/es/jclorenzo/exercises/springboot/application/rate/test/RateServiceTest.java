package es.jclorenzo.exercises.springboot.application.rate.test;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import es.jclorenzo.exercises.springboot.application.exception.DateRangeOverlapException;
import es.jclorenzo.exercises.springboot.application.rate.RateServiceImpl;
import es.jclorenzo.exercises.springboot.domain.rate.Rate;
import es.jclorenzo.exercises.springboot.domain.rate.repository.RateRepository;
import es.jclorenzo.exercises.springboot.domain.rate.vo.CurrencyVO;
import es.jclorenzo.exercises.springboot.domain.rate.vo.PriceVO;
import es.jclorenzo.exercises.springboot.domain.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class RateServiceTest.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public class RateServiceTest {

	/** The rate repo. */
	@Mock
	private RateRepository rateRepo;

	/** The currency client. */
	@Mock
	private CurrencyService currencyService;

	/** The rate service. */
	@InjectMocks
	private RateServiceImpl rateService;

	/**
	 * Gets the euro currency.
	 *
	 * @return the euro currency
	 */
	private CurrencyVO getEuroCurrency() {
		return new CurrencyVO("€", "EUR", 2);
	}

	/**
	 * Gets the dollar currency.
	 *
	 * @return the dollar currency
	 */
	private CurrencyVO getDollarCurrency() {
		return new CurrencyVO("$", "USD", 2);
	}

	/**
	 * Gets the euro price.
	 *
	 * @return the euro price
	 */
	private PriceVO getEuroPrice() {
		return new PriceVO(1234, this.getEuroCurrency());
	}

	/**
	 * Gets the defaul rate.
	 *
	 * @return the defaul rate
	 */
	private Rate getRate() {
		final int rateId = 1;
		final int brandId = 1;
		final int productId = 1;

		return new Rate(
				rateId,
				brandId,
				productId,
				LocalDate.now(),
				LocalDate.now().plusMonths(1),
				this.getEuroPrice());
	}

	/**
	 * Adds the new rate with invalid date range.
	 */
	@Test
	void addNewRate() {

		final int productId = 1;
		final int brandId = 1;
		final double ratePrice = 12.34;
		final String currencyCode = "EUR";
		final LocalDate startDate = LocalDate.now();
		final LocalDate endDate = LocalDate.now().plusMonths(1);

		final String expectedRateFormattedPrice = "12,34 € (EUR)";

		Mockito.when(this.currencyService.findByCode(currencyCode)).thenReturn(this.getEuroCurrency());
		Mockito.when(this.rateRepo.save(ArgumentMatchers.any(Rate.class))).thenReturn(this.getRate());

		Assertions.assertDoesNotThrow(() -> {
			final Rate rate =
					this.rateService.addNewRate(productId, brandId, startDate, endDate, ratePrice, currencyCode);

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

		final int productId = 1;
		final int brandId = 1;
		final LocalDate startDate = LocalDate.now();
		final LocalDate endDate = LocalDate.now().plusMonths(1);
		final double ratePrice = 10.20;
		final String currencyCode = "EUR";

		Mockito.when(this.rateRepo.exists(brandId, productId, startDate, endDate)).thenReturn(true);

		Assertions.assertThrows(DateRangeOverlapException.class, () -> {
			this.rateService.addNewRate(productId, brandId, startDate, endDate, ratePrice, currencyCode);

		});
	}

	/**
	 * Adds the new with unkown currency.
	 */
	@Test
	void addNewWithUnkownCurrency() {

		Mockito.when(this.currencyService.findByCode(ArgumentMatchers.any(String.class)))
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
		final double ratePrice = 12.34;
		final String currencyCode = "EUR";
		final LocalDate startDate = LocalDate.now();
		final LocalDate endDate = LocalDate.now().minusMonths(1);

		Mockito.when(this.currencyService.findByCode(ArgumentMatchers.anyString())).thenReturn(this.getEuroCurrency());

		Assertions.assertEquals("Invalid date range",
				Assertions.assertThrows(IllegalArgumentException.class, () -> {
					this.rateService.addNewRate(
							productId,
							brandId,
							startDate,
							endDate,
							ratePrice,
							currencyCode);
				}).getMessage());
	}

	/**
	 * Search rate.
	 */
	@Test
	void searchRate() {
		final int brandId = 1;
		final int productId = 2;
		final LocalDate effectiveDate = LocalDate.now();

		final String expectedRateFormattedPrice = "12,34 € (EUR)";

		Mockito.when(
				this.rateRepo.search(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
						ArgumentMatchers.any(LocalDate.class)))
				.thenReturn(this.getRate());

		Assertions.assertDoesNotThrow(() -> {
			final Rate rate = this.rateService.search(productId, brandId, effectiveDate);
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

		final int brandId = 1;
		final int productId = 1;
		final LocalDate effectiveDate = LocalDate.now();

		Mockito.when(this.rateRepo.search(brandId, productId, effectiveDate))
				.thenThrow(NoSuchElementException.class);

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.rateService.search(brandId, productId, effectiveDate);
		});

	}

	/**
	 * Find by id non existant rate.
	 */
	@Test
	void findByIdNonExistentRate() {

		final int rateId = 1;

		Mockito.when(this.rateRepo.findById(rateId))
				.thenThrow(NoSuchElementException.class);

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

		final String expectedRateFormattedPrice = "12,34 € (EUR)";

		Mockito.when(this.rateRepo.findById(rateId)).thenReturn(this.getRate());

		Assertions.assertDoesNotThrow(() -> {
			final Rate rate = this.rateService.findById(rateId);
			Assertions.assertNotNull(rate);
			Assertions.assertEquals(expectedRateFormattedPrice, rate.formattedPrice());
		});

	}

	/**
	 * Update rate price.
	 */
	@Test
	void updateRatePrice() {

		final int rateId = 1;
		final String dollarCurrencyCode = "USD";

		final double newPrice = 4.123D;
		final String expectedRateFormattedPrice = "4,12 $ (USD)";

		Mockito.when(this.currencyService.findByCode(dollarCurrencyCode)).thenReturn(this.getDollarCurrency());

		Mockito.when(this.rateRepo.findById(rateId)).thenReturn(this.getRate());

		Mockito.when(this.rateRepo.save(ArgumentMatchers.any(Rate.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		Assertions.assertDoesNotThrow(() -> {
			final Rate rate = this.rateService.updatePrice(rateId, newPrice, dollarCurrencyCode);
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

		Mockito.when(this.rateRepo.findById(rateId)).thenThrow(NoSuchElementException.class);

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
