package es.jclorenzo.exercises.springboot.currency.client.impl;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import es.jclorenzo.exercises.springboot.currency.client.CurrencyRemoteClient;
import es.jclorenzo.exercises.springboot.currency.client.enums.CurrencyEndpoints;
import es.jclorenzo.exercises.springboot.currency.model.Currency;

/**
 * The Class CurrencyClientImpl.
 */
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
		return this.currecyRestClient
				.get()
				.uri(CurrencyEndpoints.CURRENCIES.getValue())
				.retrieve().body(new ParameterizedTypeReference<List<Currency>>() {
				});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Currency getCurrencyByCode(final String currencyCode) {
		return this.currecyRestClient
				.get()
				.uri(CurrencyEndpoints.CURRENCY.getValue(), currencyCode)
				.retrieve().body(Currency.class);
	}

}
