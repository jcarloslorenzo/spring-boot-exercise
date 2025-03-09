package es.jclorenzo.exercises.springboot.repository.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import es.jclorenzo.exercises.springboot.repository.entity.RateEntity;
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
public class RateSearchSpecification implements Specification<RateEntity> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2960487975611398691L;

	/** The brand id. */
	private final Integer brandId;

	/** The product id. */
	private final Integer productId;

	/** The effective date. */
	private final LocalDate effectiveDate;

	/**
	 * Gets a new specification instance.
	 *
	 * @param brandId       the brand ID
	 * @param productId     the product ID
	 * @param effectiveDate the effective date
	 * @return the new specification instance
	 */
	public static RateSearchSpecification getNewInstance(
			final int brandId,
			final int productId,
			final LocalDate effectiveDate) {

		return new RateSearchSpecification(brandId, productId, effectiveDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Predicate toPredicate(final Root<RateEntity> root, final CriteriaQuery<?> query,
			final CriteriaBuilder criteriaBuilder) {
		return criteriaBuilder.and(
				criteriaBuilder.equal(root.<Integer>get("productId"), this.productId),
				criteriaBuilder.equal(root.<Integer>get("brandId"), this.brandId),
				criteriaBuilder.lessThanOrEqualTo(root.<LocalDate>get("startDate"), this.effectiveDate),
				criteriaBuilder.greaterThanOrEqualTo(root.<LocalDate>get("endDate"), this.effectiveDate));
	}

}
