package es.jclorenzo.exercises.springboot.infrastructure.adapter.currency.dto;

import java.util.List;

import org.mapstruct.Mapper;

import es.jclorenzo.exercises.springboot.domain.rate.vo.CurrencyVO;

/**
 * The Interface CurrencyVOMapper.
 */
@Mapper(componentModel = "spring")
public interface RemoteCurrencyMapper {

	/**
	 * To domain entity.
	 *
	 * @param source the source
	 * @return the currency VO
	 */
	CurrencyVO toDomainEntity(RemoteCurrency source);

	/**
	 * To domain entity list.
	 *
	 * @param body the body
	 * @return the list
	 */
	List<CurrencyVO> toDomainEntityList(List<RemoteCurrency> body);
}
