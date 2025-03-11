package es.jclorenzo.exercises.springboot.currency.client.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import es.jclorenzo.exercises.springboot.currency.client.CurrencyRemoteClient;
import es.jclorenzo.exercises.springboot.currency.client.enums.CurrencyEndpoints;
import es.jclorenzo.exercises.springboot.currency.exception.RemoteSystemAccessException;
import es.jclorenzo.exercises.springboot.currency.model.Currency;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CurrencyClientImpl.
 */
@Slf4j
@Service
public class CurrencyRemoteClientImpl implements CurrencyRemoteClient {

	/** The currecy rest client. */
	private final RestClient currecyRestClient;

	/**
	 * Instantiates a new currency client impl.
	 *
	 * @param currecyRestClient the currecy rest client
	 */
	public CurrencyRemoteClientImpl(final RestClient currecyRestClient) {
		this.currecyRestClient = currecyRestClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Currency> getCurrencies() {
		try {
			return this.currecyRestClient
					.get()
					.uri(CurrencyEndpoints.CURRENCIES.getValue())
					.retrieve().body(new ParameterizedTypeReference<List<Currency>>() {
					});
		} catch (final Exception e) {
			throw new RemoteSystemAccessException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Currency getCurrencyByCode(final String currencyCode) {
		try {
			return this.currecyRestClient
					.get()
					.uri(CurrencyEndpoints.CURRENCY.getValue(), currencyCode)
					.retrieve().body(Currency.class);
		} catch (final Exception e) {
			if (HttpClientErrorException.class.isAssignableFrom(e.getClass())
					&& HttpStatus.NOT_FOUND.equals(((HttpClientErrorException) e).getStatusCode())) {
				throw new NoSuchElementException(String.format("No currency '%s' found", currencyCode));
			}
			throw new RemoteSystemAccessException(e.getMessage(), e);
		}
	}

}
