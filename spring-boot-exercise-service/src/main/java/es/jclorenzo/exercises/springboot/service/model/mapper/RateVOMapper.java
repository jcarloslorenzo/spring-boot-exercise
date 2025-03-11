package es.jclorenzo.exercises.springboot.service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import es.jclorenzo.exercises.springboot.currency.client.CurrencyRemoteClient;
import es.jclorenzo.exercises.springboot.repository.entity.RateEntity;
import es.jclorenzo.exercises.springboot.service.model.RateVO;

/**
 * The Interface RateMapper.
 */
@Mapper(componentModel = "spring")
public abstract class RateVOMapper {

	/** The currency client. */
	@Autowired
	protected CurrencyRemoteClient currencyClient;

	/** The currency mapper. */
	@Autowired
	protected CurrencyVOMapper currencyMapper;

	/**
	 * From entity.
	 *
	 * @param source the source
	 * @return the rate
	 */
	@Mapping(target = "effectiveStartDate", source = "startDate")
	@Mapping(target = "effectiveEndDate", source = "endDate")
	@Mapping(target = "price",
			expression = "java(new PriceVO(source.getPrice(), currencyMapper.fromRecord(currencyClient.getCurrencyByCode(source.getCurrencyCode()))))")
	public abstract RateVO fromEntity(RateEntity source);

	/**
	 * To entity.
	 *
	 * @param source the source
	 * @return the rate entity
	 */
	@Mapping(target = "startDate", source = "effectiveStartDate")
	@Mapping(target = "endDate", source = "effectiveEndDate")
	@Mapping(target = "price", expression = "java(source.getPrice())")
	@Mapping(target = "currencyCode", expression = "java(source.getCurrencyCode())")
	public abstract RateEntity toEntity(RateVO source);
}
