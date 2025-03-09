package es.jclorenzo.exercises.springboot.api.controller.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import es.jclorenzo.exercises.springboot.controller.RatesApi;
import es.jclorenzo.exercises.springboot.model.Rate;
import es.jclorenzo.exercises.springboot.model.RateCreate;
import es.jclorenzo.exercises.springboot.model.RateUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * The Class RatesController.
 */
@RestController
public class RatesController implements RatesApi {

	/**
	 * Instantiates a new rates controller.
	 */
	public RatesController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Rate> addRate(@Valid final RateCreate rateCreate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Void> deleteRate(final Long rateId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<List<Rate>> getRate(final Long rateId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Rate> searchRate(@NotNull @Valid final Long brandId, @NotNull @Valid final Long productId,
			@Valid final LocalDate effectiveDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Rate> updateRate(final Long rateId, @Valid final RateUpdate rateUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

}
