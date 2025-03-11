package es.jclorenzo.exercises.springboot.service.model.mapper;

import org.mapstruct.Mapper;

import es.jclorenzo.exercises.springboot.currency.model.Currency;
import es.jclorenzo.exercises.springboot.service.model.CurrencyVO;

/**
 * The Interface CurrencyVOMapper.
 */
@Mapper(componentModel = "spring")
public interface CurrencyVOMapper {

	/**
	 * From record.
	 *
	 * @param source the source
	 * @return the currency VO
	 */
	CurrencyVO fromRecord(Currency source);
}
