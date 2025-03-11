package es.jclorenzo.exercises.springboot.currency.test;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

import es.jclorenzo.exercises.springboot.currency.client.enums.CurrencyEndpoints;
import es.jclorenzo.exercises.springboot.currency.client.impl.CurrencyRemoteClientImpl;
import es.jclorenzo.exercises.springboot.currency.exception.RemoteSystemAccessException;
import es.jclorenzo.exercises.springboot.currency.model.Currency;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CurrencyClientTest.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public class CurrencyClientTest {

	/** The rest template. */
	@Mock
	private RestClient restClient;

	/** The request headers uri spec. */
	@Mock
	@SuppressWarnings("rawtypes")
	private RequestHeadersUriSpec requestHeadersUriSpec;

	/** The request headers spec. */
	@Mock
	@SuppressWarnings("rawtypes")
	private RequestHeadersSpec requestHeadersSpec;

	/** The response spec. */
	@Mock
	private ResponseSpec responseSpec;

	/** The user remote service. */
	@InjectMocks
	private CurrencyRemoteClientImpl currencyClient;

	/** The currencies. */
	private final List<Currency> currencies = List.of(
			new Currency("€", "EUR", 2),
			new Currency("$", "USD", 2));

	/**
	 * Find currencies test.
	 */
	@Test
	@SuppressWarnings({ "unchecked" })
	public void findCurrenciesTest() {

		Mockito.when(this.restClient.get())
				.thenReturn(this.requestHeadersUriSpec);
		Mockito.when(this.requestHeadersUriSpec.uri(CurrencyEndpoints.CURRENCIES.getValue()))
				.thenReturn(this.requestHeadersSpec);
		Mockito.when(this.requestHeadersSpec.retrieve())
				.thenReturn(this.responseSpec);
		Mockito.when(this.responseSpec.body(new ParameterizedTypeReference<List<Currency>>() {
		})).thenReturn(this.currencies);

		Assertions.assertDoesNotThrow(() -> {
			final List<Currency> result = this.currencyClient.getCurrencies();
			Assertions.assertNotNull(result);
			Assertions.assertFalse(result.isEmpty());
		});
	}

	/**
	 * Find currency euro test.
	 */
	@Test
	@SuppressWarnings({ "unchecked" })
	public void findCurrencyEuroTest() {
		final String currencyCode = "EUR";

		Mockito.when(this.restClient.get())
				.thenReturn(this.requestHeadersUriSpec);
		Mockito.when(this.requestHeadersUriSpec.uri(CurrencyEndpoints.CURRENCY.getValue(), currencyCode))
				.thenReturn(this.requestHeadersSpec);
		Mockito.when(this.requestHeadersSpec.retrieve())
				.thenReturn(this.responseSpec);
		Mockito.when(this.responseSpec.body(Currency.class))
				.thenReturn(this.currencies.get(0));

		Assertions.assertDoesNotThrow(() -> {
			final Currency result = this.currencyClient.getCurrencyByCode(currencyCode);
			Assertions.assertNotNull(result);
			Assertions.assertEquals(currencyCode, result.code());
		});
	}

	/**
	 * Not found error.
	 */
	@Test
	@SuppressWarnings({ "unchecked" })
	public void notFoundError() {
		final String currencyCode = "XXX";

		final HttpClientErrorException internalErrorException = new HttpClientErrorException(
				HttpStatus.NOT_FOUND, "Not Found");

		Mockito.when(this.restClient.get())
				.thenReturn(this.requestHeadersUriSpec);
		Mockito.when(this.requestHeadersUriSpec.uri(CurrencyEndpoints.CURRENCY.getValue(), currencyCode))
				.thenReturn(this.requestHeadersSpec);
		Mockito.when(this.requestHeadersSpec.retrieve()).thenThrow(internalErrorException);

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.currencyClient.getCurrencyByCode(currencyCode);
		});
	}

	/**
	 * Remote internal server error.
	 */
	@Test
	@SuppressWarnings({ "unchecked" })
	public void remoteInternalServerError() {
		final String currencyCode = "XXX";

		final HttpServerErrorException internalErrorException = new HttpServerErrorException(
				HttpStatus.INTERNAL_SERVER_ERROR, "Simulated Internal Server Error");

		Mockito.when(this.restClient.get())
				.thenReturn(this.requestHeadersUriSpec);
		Mockito.when(this.requestHeadersUriSpec.uri(CurrencyEndpoints.CURRENCY.getValue(), currencyCode))
				.thenReturn(this.requestHeadersSpec);
		Mockito.when(this.requestHeadersSpec.retrieve()).thenThrow(internalErrorException);

		Assertions.assertThrows(RemoteSystemAccessException.class, () -> {
			this.currencyClient.getCurrencyByCode(currencyCode);
		});
	}

}
