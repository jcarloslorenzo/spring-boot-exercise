package es.jclorenzo.exercises.springboot.infrastructure.persistence.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import es.jclorenzo.exercises.springboot.infrastructure.persistence.entity.RateEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Specification for performing a rate search by product ID, brand ID, and
 * effective date.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RateSearchByProductAndBrandSpecification implements Specification<RateEntity> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2960487975611398691L;

	/** The brand id. */
	private final Integer brandId;

	/** The product id. */
	private final Integer productId;

	/**
	 * Gets a new specification instance.
	 *
	 * @param brandId   the brand ID
	 * @param productId the product ID
	 * @return the new specification instance
	 */
	public static RateSearchByProductAndBrandSpecification getNewInstance(
			final int brandId,
			final int productId) {

		return new RateSearchByProductAndBrandSpecification(brandId, productId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Predicate toPredicate(final Root<RateEntity> root, final CriteriaQuery<?> query,
			final CriteriaBuilder criteriaBuilder) {
		return criteriaBuilder.and(
				criteriaBuilder.equal(root.<Integer>get("productId"), this.productId),
				criteriaBuilder.equal(root.<Integer>get("brandId"), this.brandId));
	}

}
