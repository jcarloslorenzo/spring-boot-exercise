package es.jclorenzo.exercises.springboot.currency.config;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * The Class ModuleConfig.
 */
@EnableConfigurationProperties
@Configuration(value = "CurrencyClientModuleConfiguration")
@ComponentScan(basePackages = { "es.jclorenzo.exercises.springboot.currency.client" })
public class ModuleConfiguration {

	/**
	 * Currency client properties.
	 *
	 * @return the currency client properties
	 */
	@Bean
	@ConfigurationProperties(prefix = "app.external.currency.client")
	CurrencyClientProperties currencyClientProperties() {
		return new CurrencyClientProperties();
	}

	/**
	 * Currecy rest client.
	 *
	 * @return the rest client
	 */
	@Bean
	RestClient currecyRestClient() {
		final CurrencyClientProperties clientProperties = this.currencyClientProperties();

		final String encodedAuth = Base64.getEncoder().encodeToString(
				String.join(":", clientProperties.getUsername(), clientProperties.getPassword()).getBytes());

		final String authHeader = "Basic " + new String(encodedAuth);

		return RestClient.builder()
				.baseUrl(clientProperties.getBaseUrl())
				.defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
				.build();
	}

	/**
	 * Currencies cache manager.
	 *
	 * @return the cache manager
	 */
	@Bean
	CacheManager currenciesCacheManager() {
		final CaffeineCacheManager cacheManager = new CaffeineCacheManager("currencies");
		cacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(10).expireAfterWrite(10, TimeUnit.MINUTES));
		return cacheManager;
	}

}
