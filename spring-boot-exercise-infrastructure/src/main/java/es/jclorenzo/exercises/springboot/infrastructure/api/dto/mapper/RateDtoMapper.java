package es.jclorenzo.exercises.springboot.infrastructure.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.jclorenzo.exercises.springboot.domain.rate.Rate;
import es.jclorenzo.exercises.springboot.infrastructure.api.rate.dto.RateDto;

/**
 * The Interface RateMapper.
 */
@Mapper(componentModel = "spring")
public abstract class RateDtoMapper {

	/**
	 * From VO.
	 *
	 * @param source the source
	 * @return the rate
	 */
	@Mapping(target = "price", expression = "java(source.formattedPrice())")
	public abstract RateDto fromDomainEntity(Rate source);

}
