package es.jclorenzo.exercises.springboot.api.test;

import java.time.LocalDate;
import java.util.Base64;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.utility.MountableFile;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import es.jclorenzo.exercises.springboot.api.controller.model.Currency;
import es.jclorenzo.exercises.springboot.api.controller.model.Rate;
import es.jclorenzo.exercises.springboot.api.controller.model.RateCreate;
import es.jclorenzo.exercises.springboot.api.controller.model.RateUpdate;
import es.jclorenzo.exercises.springboot.api.test.config.ApiTestConfiguration;

/**
 * The Class RateControllerTest.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApiTestConfiguration.class)
public class RateControllerTest {

	/** The rest template. */
	@Autowired
	private TestRestTemplate restTemplate;

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
	static void beforeAll() {
		RateControllerTest.wiremockContainer.start();
		System.setProperty("WIREMOCK_BASE_URL",
				"http://localhost:" + RateControllerTest.wiremockContainer.getMappedPort(8080));

	}

	/**
	 * Sotp.
	 */
	@AfterAll
	static void afterAll() {
		if (RateControllerTest.wiremockContainer.isRunning()) {
			RateControllerTest.wiremockContainer.stop();
		}
	}

	/**
	 * Test no authenticated.
	 */
	@Test
	void testNoAuthenticatedRequest() {

		final String url = "/v1/rates/1";

		Assertions.assertDoesNotThrow(() -> {
			final HttpHeaders headers = new HttpHeaders();

			final HttpEntity<Rate> entity = new HttpEntity<>(headers);
			final ResponseEntity<Rate> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, Rate.class);

			this.restTemplate.getForEntity(url, Rate.class);

			Assertions.assertTrue(response.getStatusCode().is4xxClientError());
			Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		});
	}

	/**
	 * Gets the rate by ID.
	 *
	 * @return the rate by ID
	 */
	@Test
	void getRateByID() {

		final String url = "/v1/rates/1";

		final String expectedPrice = "15.5 € (EUR)";
		Assertions.assertDoesNotThrow(() -> {
			final HttpHeaders headers = this.initHeadersWithAuthorization();

			final HttpEntity<Rate> entity = new HttpEntity<>(headers);
			final ResponseEntity<Rate> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, Rate.class);

			this.restTemplate.getForEntity(url, Rate.class);

			Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
			Assertions.assertNotNull(response.getBody());
			Assertions.assertEquals(expectedPrice, response.getBody().getPrice());
		});
	}

	/**
	 * Gets the non existent rate by ID.
	 *
	 * @return the non existent rate by ID
	 */
	@Test
	void getNonExistentRateByID() {

		final String url = "/v1/rates/1234";

		Assertions.assertDoesNotThrow(() -> {

			final HttpHeaders headers = this.initHeadersWithAuthorization();

			final HttpEntity<String> entity = new HttpEntity<>(headers);
			final ResponseEntity<Rate> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, Rate.class);

			this.restTemplate.getForEntity(url, Rate.class);

			Assertions.assertTrue(response.getStatusCode().is4xxClientError());
			Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		});
	}

	/**
	 * Adds the rate.
	 */
	@Test
	void addRate() {
		final String url = "/v1/rates";

		final String expectedPrice = "12.5 € (EUR)";

		final RateCreate newRate = new RateCreate(
				1,
				1,
				LocalDate.now(),
				LocalDate.now().plusMonths(1),
				12.50D,
				Currency.EUR);

		Assertions.assertDoesNotThrow(() -> {

			final HttpHeaders headers = this.initHeadersWithAuthorization();

			final HttpEntity<RateCreate> entity = new HttpEntity<>(newRate, headers);
			final ResponseEntity<Rate> response = this.restTemplate.exchange(url, HttpMethod.POST, entity, Rate.class);

			this.restTemplate.getForEntity(url, Rate.class);

			Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
			Assertions.assertNotNull(response.getBody());
			Assertions.assertEquals(expectedPrice, response.getBody().getPrice());
		});
	}

	/**
	 * Adds the rate with overlaped data range.
	 */
	@Test
	void addRateWithOverlapedDataRange() {
		final String url = "/v1/rates";

		final RateCreate newRate = new RateCreate(
				1,
				1,
				LocalDate.of(2022, 3, 14),
				LocalDate.of(2022, 8, 14),
				12.50D,
				Currency.EUR);

		Assertions.assertDoesNotThrow(() -> {

			final HttpHeaders headers = this.initHeadersWithAuthorization();

			final HttpEntity<RateCreate> entity = new HttpEntity<>(newRate, headers);
			final ResponseEntity<Rate> response = this.restTemplate.exchange(url, HttpMethod.POST, entity, Rate.class);

			this.restTemplate.getForEntity(url, Rate.class);

			Assertions.assertTrue(response.getStatusCode().is4xxClientError());
			Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		});
	}

	/**
	 * Update price.
	 */
	@Test
	void updatePrice() {
		final String url = "/v1/rates/5";

		final String expectedPrice = "12.5 $ (USD)";

		final RateUpdate newRatePrice = new RateUpdate(
				12.50D,
				Currency.USD);

		Assertions.assertDoesNotThrow(() -> {

			final HttpHeaders headers = this.initHeadersWithAuthorization();

			final HttpEntity<RateUpdate> entity = new HttpEntity<>(newRatePrice, headers);
			final ResponseEntity<Rate> response = this.restTemplate.exchange(url, HttpMethod.PUT, entity, Rate.class);

			this.restTemplate.getForEntity(url, Rate.class);

			Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
			Assertions.assertNotNull(response.getBody());
			Assertions.assertEquals(expectedPrice, response.getBody().getPrice());
		});
	}

	/**
	 * Search rate.
	 */
	@Test
	void searchRate() {

		final String expectedPrice = "20.5 € (EUR)";

		final String url = "/v1/rates?product_id=1&brand_id=2&effective_date=2022-03-14";

		Assertions.assertDoesNotThrow(() -> {
			final HttpHeaders headers = this.initHeadersWithAuthorization();

			final HttpEntity<Rate> entity = new HttpEntity<>(headers);
			final ResponseEntity<Rate> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, Rate.class);

			this.restTemplate.getForEntity(url, Rate.class);

			Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
			Assertions.assertNotNull(response.getBody());
			Assertions.assertEquals(expectedPrice, response.getBody().getPrice());
		});
	}

	/**
	 * Delete rate by ID.
	 */
	@Test
	@Order(Integer.MAX_VALUE)
	void deleteRateByID() {

		final String url = "/v1/rates/1";

		Assertions.assertDoesNotThrow(() -> {
			final HttpHeaders headers = this.initHeadersWithAuthorization();

			final HttpEntity<Void> entity = new HttpEntity<>(headers);
			final ResponseEntity<Rate> response = this.restTemplate.exchange(url, HttpMethod.DELETE, entity,
					Rate.class);

			this.restTemplate.getForEntity(url, Rate.class);

			Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
			Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		});
	}

	/**
	 * No currency service available.
	 */
	@Test
	@Order(Integer.MAX_VALUE)
	void noCurrencyServiceAvailable() {
		RateControllerTest.wiremockContainer.stop();
		final String url = "/v1/rates/6";

		Assertions.assertDoesNotThrow(() -> {
			final HttpHeaders headers = this.initHeadersWithAuthorization();

			final HttpEntity<Rate> entity = new HttpEntity<>(headers);
			final ResponseEntity<Rate> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, Rate.class);

			this.restTemplate.getForEntity(url, Rate.class);

			Assertions.assertTrue(response.getStatusCode().is4xxClientError());
			Assertions.assertEquals(HttpStatus.FAILED_DEPENDENCY, response.getStatusCode());
		});
	}

	/**
	 * Inits the headers with authorization.
	 *
	 * @return the http headers
	 */
	private HttpHeaders initHeadersWithAuthorization() {
		final HttpHeaders headers = new HttpHeaders();
		final String auth = "user:password";
		final String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
		headers.set("Authorization", "Basic " + encodedAuth);
		return headers;
	}
}
