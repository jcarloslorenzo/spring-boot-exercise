package es.jclorenzo.exercises.springboot.api.controller.impl;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import es.jclorenzo.exercises.springboot.api.controller.RatesApi;
import es.jclorenzo.exercises.springboot.api.controller.model.Rate;
import es.jclorenzo.exercises.springboot.api.controller.model.RateCreate;
import es.jclorenzo.exercises.springboot.api.controller.model.RateUpdate;
import es.jclorenzo.exercises.springboot.api.controller.model.mapper.RateMapper;
import es.jclorenzo.exercises.springboot.service.RateService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * The Class RatesController.
 */
@RestController
public class RateController implements RatesApi {

	/** The rate service. */
	private final RateService rateService;

	/** The rate mapper. */
	private final RateMapper rateMapper;

	/**
	 * Instantiates a new rates controller.
	 *
	 * @param rateService the rate service
	 * @param rateMapper  the rate mapper
	 */
	public RateController(final RateService rateService , final RateMapper rateMapper) {
		this.rateMapper = rateMapper;
		this.rateService = rateService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Rate> addRate(@Valid final RateCreate rateCreate) {

		return ResponseEntity.ok(
				this.rateMapper.fromVO(
						this.rateService.addNewRate(
								rateCreate.getProductId(),
								rateCreate.getBrandId(),
								rateCreate.getEffectiveStartDate(),
								rateCreate.getEffectiveEndDate(),
								rateCreate.getPrice(),
								rateCreate.getCurrency().getValue())));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Void> deleteRate(final Integer rateId) {
		this.rateService.delete(rateId);
		return ResponseEntity.noContent().build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Rate> getRate(final Integer rateId) {

		return ResponseEntity.ok(
				this.rateMapper.fromVO(
						this.rateService.findById(rateId)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Rate> searchRate(
			@NotNull @Valid final Integer brandId,
			@NotNull @Valid final Integer productId,
			@Valid final LocalDate effectiveDate) {

		return ResponseEntity.ok(
				this.rateMapper.fromVO(
						this.rateService.search(
								brandId,
								productId,
								effectiveDate == null ? LocalDate.now() : effectiveDate)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Rate> updateRate(final Integer rateId, @Valid final RateUpdate rateUpdate) {
		return ResponseEntity.ok(
				this.rateMapper.fromVO(
						this.rateService.updatePrice(
								rateId,
								rateUpdate.getPrice(),
								rateUpdate.getCurrency().getValue())));
	}

}
