package es.jclorenzo.exercises.springboot.infrastructure.adapter.currency;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import es.jclorenzo.exercises.springboot.domain.rate.vo.CurrencyVO;
import es.jclorenzo.exercises.springboot.domain.service.CurrencyService;
import es.jclorenzo.exercises.springboot.infrastructure.adapter.currency.dto.RemoteCurrency;
import es.jclorenzo.exercises.springboot.infrastructure.adapter.currency.dto.RemoteCurrencyMapper;
import es.jclorenzo.exercises.springboot.infrastructure.exception.RemoteSystemAccessException;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CurrencyClientImpl.
 */
@Slf4j
@Service
public class CurrencyRemoteClientImpl implements CurrencyService {

	/** The currecy rest client. */
	private final RestClient currecyRestClient;

	/** The remote currency mapper. */
	private final RemoteCurrencyMapper remoteCurrencyMapper;

	/**
	 * Instantiates a new currency client impl.
	 *
	 * @param currecyRestClient    the currecy rest client
	 * @param remoteCurrencyMapper the remote currency mapper
	 */
	public CurrencyRemoteClientImpl(
			final RestClient currecyRestClient ,
			final RemoteCurrencyMapper remoteCurrencyMapper) {

		this.currecyRestClient = currecyRestClient;
		this.remoteCurrencyMapper = remoteCurrencyMapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CurrencyVO> getCurrencies() {
		try {
			return this.remoteCurrencyMapper.toDomainEntityList(
					this.currecyRestClient
							.get()
							.uri(CurrencyEndpoints.CURRENCIES.getValue())
							.retrieve().body(new ParameterizedTypeReference<List<RemoteCurrency>>() {
							}));
		} catch (final Exception e) {
			throw new RemoteSystemAccessException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Cacheable(value = "currencies", key = "#currencyCode", cacheManager = "currenciesCacheManager")
	public CurrencyVO findByCode(final String currencyCode) {
		try {
			return this.remoteCurrencyMapper.toDomainEntity(
					this.currecyRestClient
							.get()
							.uri(CurrencyEndpoints.CURRENCY.getValue(), currencyCode)
							.retrieve().body(RemoteCurrency.class));
		} catch (final Exception e) {
			if (HttpClientErrorException.class.isAssignableFrom(e.getClass())
					&& HttpStatus.NOT_FOUND.equals(((HttpClientErrorException) e).getStatusCode())) {
				throw new NoSuchElementException(String.format("No currency '%s' found", currencyCode));
			}
			throw new RemoteSystemAccessException(e.getMessage(), e);
		}
	}

}
