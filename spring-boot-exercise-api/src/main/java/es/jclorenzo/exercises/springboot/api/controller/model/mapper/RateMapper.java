package es.jclorenzo.exercises.springboot.api.controller.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.jclorenzo.exercises.springboot.model.Rate;
import es.jclorenzo.exercises.springboot.service.model.RateVO;

/**
 * The Interface RateMapper.
 */
@Mapper(componentModel = "spring")
public interface RateMapper {

	/**
	 * From VO.
	 *
	 * @param source the source
	 * @return the rate
	 */
	@Mapping(target = "price", expression = "java(source.formattedPrice())")
	Rate fromVO(RateVO source);

}
