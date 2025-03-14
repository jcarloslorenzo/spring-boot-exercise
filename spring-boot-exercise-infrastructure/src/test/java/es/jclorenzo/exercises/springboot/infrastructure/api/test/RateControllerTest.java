package es.jclorenzo.exercises.springboot.infrastructure.api.test;

import java.time.LocalDate;
import java.util.Base64;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.jclorenzo.exercises.springboot.application.exception.DateRangeOverlapException;
import es.jclorenzo.exercises.springboot.domain.rate.Rate;
import es.jclorenzo.exercises.springboot.domain.rate.vo.CurrencyVO;
import es.jclorenzo.exercises.springboot.domain.rate.vo.PriceVO;
import es.jclorenzo.exercises.springboot.domain.service.RateService;
import es.jclorenzo.exercises.springboot.infrastructure.api.rate.dto.CurrencyEnum;
import es.jclorenzo.exercises.springboot.infrastructure.api.rate.dto.RateCreateDto;
import es.jclorenzo.exercises.springboot.infrastructure.api.rate.dto.RateUpdateDto;
import es.jclorenzo.exercises.springboot.infrastructure.api.test.config.ControllerTestConfiguration;

/**
 * The Class RateControllerTest.
 */
@WebMvcTest
@ContextConfiguration(classes = ControllerTestConfiguration.class)
public class RateControllerTest {
	/** The mock mvc. */
	@Autowired
	private MockMvc mockMvc;

	/** The rate service. */
	@MockitoBean
	private RateService rateService;

	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;

	/** The authorization header content. */
	private final String authorizationHeaderContent =
			"Basic ".concat(Base64.getEncoder().encodeToString("user:password".getBytes()));

	/**
	 * Gets the euro currency.
	 *
	 * @return the euro currency
	 */
	private CurrencyVO getEuroCurrency() {
		return new CurrencyVO("€", "EUR", 2);
	}

	/**
	 * Gets the euro price.
	 *
	 * @return the euro price
	 */
	private PriceVO getEuroPrice() {
		return new PriceVO(1234, this.getEuroCurrency());
	}

	/**
	 * Gets the defaul rate.
	 *
	 * @return the defaul rate
	 */
	private Rate getRate() {
		final int rateId = 1;
		final int brandId = 1;
		final int productId = 1;
		return new Rate(rateId, brandId, productId, LocalDate.now(), LocalDate.now().plusMonths(1),
				this.getEuroPrice());
	}

	/**
	 * Unauthorized access.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void testUnauthorizedAccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rates/1"))
				.andExpect(MockMvcResultMatchers.status().is(401));
	}

	/**
	 * Test get rate by ID.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void testGetRateByID() throws Exception {

		final String url = "/v1/rates/1";
		final String expectedPrice = "12,34 € (EUR)";

		Mockito.when(this.rateService.findById(ArgumentMatchers.anyInt())).thenReturn(this.getRate());

		this.mockMvc.perform(
				MockMvcRequestBuilders.get(url).header(HttpHeaders.AUTHORIZATION, this.authorizationHeaderContent))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("price").value(expectedPrice));
	}

	/**
	 * Gets the non existent rate by ID.
	 *
	 * @return the non existent rate by ID
	 * @throws Exception the exception
	 */
	@Test
	void getNonExistentRateByID() throws Exception {

		final String url = "/v1/rates/1";

		Mockito.when(this.rateService.findById(ArgumentMatchers.anyInt())).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(
				MockMvcRequestBuilders.get(url).header(HttpHeaders.AUTHORIZATION, this.authorizationHeaderContent))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Adds the rate.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void addRate() throws Exception {
		final String url = "/v1/rates";
		final String expectedPrice = "12,34 € (EUR)";

		Mockito.when(this.rateService.addNewRate(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
				ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class),
				ArgumentMatchers.anyDouble(), ArgumentMatchers.anyString()))
				.thenReturn(this.getRate());

		final RateCreateDto newRate =
				new RateCreateDto(1, 1, LocalDate.now(), LocalDate.now().plusMonths(1), 12.34D, CurrencyEnum.EUR);

		this.mockMvc.perform(
				MockMvcRequestBuilders.post(url)
						.contentType(MediaType.APPLICATION_JSON)
						.content(this.objectMapper.writeValueAsString(newRate))
						.header(HttpHeaders.AUTHORIZATION, this.authorizationHeaderContent))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("price").value(expectedPrice));
	}

	/**
	 * Adds the rate with overlaped data range.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void addRateWithOverlapedDataRange() throws Exception {
		final String url = "/v1/rates";

		Mockito.when(this.rateService.addNewRate(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
				ArgumentMatchers.any(LocalDate.class), ArgumentMatchers.any(LocalDate.class),
				ArgumentMatchers.anyDouble(), ArgumentMatchers.anyString()))
				.thenThrow(DateRangeOverlapException.class);

		final RateCreateDto newRate =
				new RateCreateDto(1, 1, LocalDate.now(), LocalDate.now().plusMonths(1), 12.34D, CurrencyEnum.EUR);

		this.mockMvc.perform(
				MockMvcRequestBuilders.post(url)
						.contentType(MediaType.APPLICATION_JSON)
						.content(this.objectMapper.writeValueAsString(newRate))
						.header(HttpHeaders.AUTHORIZATION, this.authorizationHeaderContent))
				.andExpect(MockMvcResultMatchers.status().isConflict());

	}

	/**
	 * Update price.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void updatePrice() throws Exception {
		final String url = "/v1/rates/1";

		final String expectedPrice = "12,34 € (EUR)";

		Mockito.when(this.rateService.updatePrice(ArgumentMatchers.anyInt(), ArgumentMatchers.anyDouble(),
				ArgumentMatchers.anyString()))
				.thenReturn(this.getRate());

		final RateUpdateDto newRatePrice = new RateUpdateDto(12.34D, CurrencyEnum.EUR);

		this.mockMvc.perform(
				MockMvcRequestBuilders.put(url)
						.contentType(MediaType.APPLICATION_JSON)
						.content(this.objectMapper.writeValueAsString(newRatePrice))
						.header(HttpHeaders.AUTHORIZATION, this.authorizationHeaderContent))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("price").value(expectedPrice));

	}

	/**
	 * Search rate.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void searchRate() throws Exception {
		final String url = "/v1/rates?product_id=1&brand_id=1&effective_date=2022-03-14";

		final String expectedPrice = "12,34 € (EUR)";

		Mockito.when(this.rateService.search(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
				ArgumentMatchers.any(LocalDate.class)))
				.thenReturn(this.getRate());

		this.mockMvc.perform(
				MockMvcRequestBuilders.get(url).header(HttpHeaders.AUTHORIZATION, this.authorizationHeaderContent))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("price").value(expectedPrice));

	}

	/**
	 * Delete rate by ID.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void deleteRateByID() throws Exception {

		final String url = "/v1/rates/1";

		this.mockMvc.perform(
				MockMvcRequestBuilders.delete(url).header(HttpHeaders.AUTHORIZATION, this.authorizationHeaderContent))
				.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
}
