package es.jclorenzo.exercises.springboot.currency.test;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import es.jclorenzo.exercises.springboot.currency.client.CurrencyRemoteClient;
import es.jclorenzo.exercises.springboot.currency.client.enums.CurrencyEndpoints;
import es.jclorenzo.exercises.springboot.currency.config.ModuleConfiguration;
import es.jclorenzo.exercises.springboot.currency.model.Currency;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CurrencyClientTest.
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ModuleConfiguration.class)
@TestPropertySource(value = "classpath:application.properties")
public class CurrencyClientTest {

	/** The rest template. */
	@Mock
	private RestTemplate restTemplate;

	/** The user remote service. */
	@Autowired
	private CurrencyRemoteClient currencyClient;

	/** The currencies. */
	private final List<Currency> currencies = List.of(
			new Currency("€", "EUR", 2),
			new Currency("$", "USD", 2));

	/**
	 * Find currencies test.
	 */
	@Test
	public void findCurrenciesTest() {

		Mockito.when(
				this.restTemplate.getForObject(
						CurrencyEndpoints.CURRENCIES.getValue(),
						List.class))
				.thenReturn(this.currencies);

		Assertions.assertDoesNotThrow(() -> {
			final List<Currency> result = this.currencyClient.getCurrencies();
			Assertions.assertNotNull(result);
			Assertions.assertFalse(result.isEmpty());
			CurrencyClientTest.log.debug("Currencies -> {}", result);
		});
	}

	/**
	 * Find currency euro test.
	 */
	@Test
	public void findCurrencyEuroTest() {
		final String currencyCode = "EUR";

		Mockito.when(
				this.restTemplate.getForObject(
						CurrencyEndpoints.CURRENCY.getValue(),
						List.class,
						currencyCode))
				.thenReturn(this.currencies);

		Assertions.assertDoesNotThrow(() -> {
			final Currency result = this.currencyClient.getCurrencyByCode(currencyCode);
			Assertions.assertNotNull(result);
			Assertions.assertEquals(currencyCode, result.code());
			CurrencyClientTest.log.debug("Currency -> {}", result);
		});
	}

}
