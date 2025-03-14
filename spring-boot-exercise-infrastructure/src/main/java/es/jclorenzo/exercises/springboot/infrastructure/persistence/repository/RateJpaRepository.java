package es.jclorenzo.exercises.springboot.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.jclorenzo.exercises.springboot.infrastructure.persistence.entity.RateEntity;

/**
 * The Interface RateRepository.
 */
public interface RateJpaRepository extends JpaRepository<RateEntity, Integer>, JpaSpecificationExecutor<RateEntity> {

	/**
	 * Exists by product id and brand id.
	 *
	 * @param productId the product id
	 * @param brandId   the brand id
	 * @return true, if successful
	 */
	boolean existsByProductIdAndBrandId(Integer productId, Integer brandId);

}
