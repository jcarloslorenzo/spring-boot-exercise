package es.jclorenzo.exercises.springboot.bootstrap.test;

import java.time.LocalDate;
import java.util.Base64;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import es.jclorenzo.exercises.springboot.bootstrap.test.config.ApiTestConfiguration;
import es.jclorenzo.exercises.springboot.bootstrap.test.extension.WireMockTestContainerExtension;
import es.jclorenzo.exercises.springboot.infrastructure.api.rate.dto.CurrencyEnum;
import es.jclorenzo.exercises.springboot.infrastructure.api.rate.dto.RateCreateDto;
import es.jclorenzo.exercises.springboot.infrastructure.api.rate.dto.RateDto;
import es.jclorenzo.exercises.springboot.infrastructure.api.rate.dto.RateUpdateDto;

/**
 * The Class RateControllerTest.
 */
@ExtendWith(WireMockTestContainerExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApiTestConfiguration.class)
public class ApiTest {

	/** The rest template. */
	@Autowired
	private TestRestTemplate restTemplate;

	/**
	 * Test no authenticated.
	 */
	@Test
	void testNoAuthenticatedRequest() {

		final String url = "/v1/rates/1";

		Assertions.assertDoesNotThrow(() -> {

			final HttpEntity<RateDto> entity = new HttpEntity<>(new HttpHeaders());
			final ResponseEntity<RateDto> response = this.restTemplate.exchange(url, HttpMethod.GET, entity,
					RateDto.class);

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

		final String expectedPrice = "15,50 € (EUR)";
		Assertions.assertDoesNotThrow(() -> {

			final HttpEntity<RateDto> entity = new HttpEntity<>(this.initHeadersWithAuthorization());
			final ResponseEntity<RateDto> response = this.restTemplate.exchange(url, HttpMethod.GET, entity,
					RateDto.class);

			this.restTemplate.getForEntity(url, RateDto.class);

			Assertions.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatus.OK));
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

			final HttpEntity<String> entity = new HttpEntity<>(this.initHeadersWithAuthorization());
			final ResponseEntity<RateDto> response = this.restTemplate.exchange(url, HttpMethod.GET, entity,
					RateDto.class);

			Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		});
	}

	/**
	 * Adds the rate.
	 */
	@Test
	void addRate() {
		final String url = "/v1/rates";

		final String expectedPrice = "12,50 € (EUR)";

		final RateCreateDto newRate =
				new RateCreateDto(1, 1, LocalDate.now(), LocalDate.now().plusMonths(1), 12.50D, CurrencyEnum.EUR);

		Assertions.assertDoesNotThrow(() -> {

			final HttpEntity<RateCreateDto> entity = new HttpEntity<>(newRate, this.initHeadersWithAuthorization());
			final ResponseEntity<RateDto> response = this.restTemplate.exchange(url, HttpMethod.POST, entity,
					RateDto.class);

			Assertions.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
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

		final RateCreateDto newRate =
				new RateCreateDto(1, 1, LocalDate.of(2022, 3, 14), LocalDate.of(2022, 8, 14), 12.50D, CurrencyEnum.EUR);

		Assertions.assertDoesNotThrow(() -> {

			final HttpEntity<RateCreateDto> entity = new HttpEntity<>(newRate, this.initHeadersWithAuthorization());
			final ResponseEntity<RateDto> response = this.restTemplate.exchange(url, HttpMethod.POST, entity,
					RateDto.class);

			Assertions.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
			Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		});
	}

	/**
	 * Update price.
	 */
	@Test
	void updatePrice() {
		final String url = "/v1/rates/5";

		final String expectedPrice = "12,50 $ (USD)";

		final RateUpdateDto newRatePrice = new RateUpdateDto(
				12.50D,
				CurrencyEnum.USD);

		Assertions.assertDoesNotThrow(() -> {

			final HttpEntity<RateUpdateDto> entity =
					new HttpEntity<>(newRatePrice, this.initHeadersWithAuthorization());
			final ResponseEntity<RateDto> response =
					this.restTemplate.exchange(url, HttpMethod.PUT, entity, RateDto.class);

			Assertions.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatus.OK));
			Assertions.assertNotNull(response.getBody());
			Assertions.assertEquals(expectedPrice, response.getBody().getPrice());
		});
	}

	/**
	 * Search rate.
	 */
	@Test
	void searchRate() {

		final String expectedPrice = "20,50 € (EUR)";

		final String url = "/v1/rates?product_id=1&brand_id=2&effective_date=2022-03-14";

		Assertions.assertDoesNotThrow(() -> {

			final HttpEntity<RateDto> entity = new HttpEntity<>(this.initHeadersWithAuthorization());
			final ResponseEntity<RateDto> response =
					this.restTemplate.exchange(url, HttpMethod.GET, entity, RateDto.class);

			Assertions.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatus.OK));
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

		final String url = "/v1/rates/5";

		Assertions.assertDoesNotThrow(() -> {

			final HttpEntity<Void> entity = new HttpEntity<>(this.initHeadersWithAuthorization());
			final ResponseEntity<Void> response =
					this.restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

			Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
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
