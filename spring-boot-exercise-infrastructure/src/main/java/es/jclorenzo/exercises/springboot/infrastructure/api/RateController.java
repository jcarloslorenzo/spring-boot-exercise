package es.jclorenzo.exercises.springboot.infrastructure.api;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import es.jclorenzo.exercises.springboot.domain.service.RateService;
import es.jclorenzo.exercises.springboot.infrastructure.api.dto.mapper.RateDtoMapper;
import es.jclorenzo.exercises.springboot.infrastructure.api.rate.RatesApi;
import es.jclorenzo.exercises.springboot.infrastructure.api.rate.dto.RateCreateDto;
import es.jclorenzo.exercises.springboot.infrastructure.api.rate.dto.RateDto;
import es.jclorenzo.exercises.springboot.infrastructure.api.rate.dto.RateUpdateDto;
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
	private final RateDtoMapper rateMapper;

	/**
	 * Instantiates a new rates controller.
	 *
	 * @param rateService the rate service
	 * @param rateMapper  the rate mapper
	 */
	public RateController(final RateService rateService, final RateDtoMapper rateMapper) {
		this.rateMapper = rateMapper;
		this.rateService = rateService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<RateDto> addRate(@Valid final RateCreateDto rateCreate) {

		return ResponseEntity
				.status(HttpStatus.CREATED).body(
						this.rateMapper.fromDomainEntity(
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
	public ResponseEntity<RateDto> getRate(final Integer rateId) {

		return ResponseEntity.ok(
				this.rateMapper.fromDomainEntity(
						this.rateService.findById(rateId)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<RateDto> searchRate(
			@NotNull @Valid final Integer brandId,
			@NotNull @Valid final Integer productId,
			@Valid final LocalDate effectiveDate) {

		return ResponseEntity.ok(
				this.rateMapper.fromDomainEntity(
						this.rateService.search(
								productId,
								brandId,
								effectiveDate == null ? LocalDate.now() : effectiveDate)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<RateDto> updateRate(final Integer rateId, @Valid final RateUpdateDto rateUpdate) {
		return ResponseEntity.ok(
				this.rateMapper.fromDomainEntity(
						this.rateService.updatePrice(
								rateId,
								rateUpdate.getPrice(),
								rateUpdate.getCurrency().getValue())));
	}

}
