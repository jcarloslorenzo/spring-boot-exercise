package es.jclorenzo.exercises.springboot.infrastructure.persistence.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import es.jclorenzo.exercises.springboot.domain.rate.Rate;
import es.jclorenzo.exercises.springboot.domain.service.CurrencyService;
import es.jclorenzo.exercises.springboot.infrastructure.persistence.entity.RateEntity;

/**
 * The Interface RateMapper.
 */
@Mapper(componentModel = "spring")
public abstract class RateEntityMapper {

	/** The currency client. */
	@Autowired
	protected CurrencyService currencyService;

	/**
	 * From domain entity.
	 *
	 * @param source the source
	 * @return the rate entity
	 */
	@Mapping(target = "price", expression = "java(source.getPriceAsInteger())")
	@Mapping(target = "currencyCode", expression = "java(source.getCurrencyCode())")
	public abstract RateEntity fromDomainEntity(Rate source);

	/**
	 * To domain entity.
	 *
	 * @param source the source
	 * @return the rate
	 */
	@Mapping(target = "price",
		expression = "java(new PriceVO(source.getPrice(), currencyService.findByCode(source.getCurrencyCode())))")
	public abstract Rate toDomainEntity(RateEntity source);
}
