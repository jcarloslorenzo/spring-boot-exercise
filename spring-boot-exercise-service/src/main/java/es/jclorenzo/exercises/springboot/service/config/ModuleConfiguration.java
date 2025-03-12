package es.jclorenzo.exercises.springboot.service.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * The Class ModuleConfiguration.
 */
@Configuration(value = "ServiceModuleConfiguration")
@ComponentScan(
		basePackages = { "es.jclorenzo.exercises.springboot.service", "es.jclorenzo.exercises.springboot.currency" })
public class ModuleConfiguration {

	/**
	 * Rates cache manager.
	 *
	 * @return the cache manager
	 */
	@Bean
	@Primary
	CacheManager ratesCacheManager() {
		final CaffeineCacheManager cacheManager = new CaffeineCacheManager("rates");
		cacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(100));
		return cacheManager;
	}

}
