package es.jclorenzo.exercises.springboot.currency.test;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.utility.MountableFile;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import es.jclorenzo.exercises.springboot.currency.client.impl.CurrencyRemoteClientImpl;
import es.jclorenzo.exercises.springboot.currency.config.ModuleConfiguration;
import es.jclorenzo.exercises.springboot.currency.model.Currency;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CurrencyClientTest.
 */
@Slf4j
//@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ModuleConfiguration.class)
public class CurrencyClientTest {

	/** The wiremock server. */
	@SuppressWarnings("resource")
	private static final WireMockContainer wiremockContainer = new WireMockContainer("wiremock/wiremock:3.12.1")
			.withCopyFileToContainer(
					MountableFile.forHostPath("src/test/resources/wiremock/mappings"),
					"/home/wiremock/mappings");

	/**
	 * Inits the.
	 */
	@BeforeAll
	static void init() {
		CurrencyClientTest.wiremockContainer.start();
		System.setProperty("WIREMOCK_BASE_URL",
				"http://localhost:" + CurrencyClientTest.wiremockContainer.getMappedPort(8080));
	}

	/**
	 * Sotp.
	 */
	@AfterAll
	static void sotp() {
		if (CurrencyClientTest.wiremockContainer.isRunning()) {
			CurrencyClientTest.wiremockContainer.stop();
		}
	}

	/** The currency client. */
	@Autowired
	private CurrencyRemoteClientImpl currencyClient;

	/**
	 * Find currencies test.
	 */
	@Test
	public void findCurrenciesTest() {

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
	public void findCurrencyEuroTest() {
		final String currencyCode = "EUR";

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
	public void notFoundError() {
		final String currencyCode = "XXX";

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.currencyClient.getCurrencyByCode(currencyCode);
		});
	}

}
