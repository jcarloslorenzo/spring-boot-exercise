package es.jclorenzo.exercises.springboot.infrastructure.persistence.repository.specification;

import java.time.LocalDate;

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
public class RateSearchByEffectiveDateSpecification implements Specification<RateEntity> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2960487975611398691L;

	/** The effective date. */
	private final LocalDate effectiveDate;

	/**
	 * Gets a new specification instance.
	 *
	 * @param effectiveDate the effective date
	 * @return the new specification instance
	 */
	public static RateSearchByEffectiveDateSpecification getNewInstance(final LocalDate effectiveDate) {

		return new RateSearchByEffectiveDateSpecification(effectiveDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Predicate toPredicate(final Root<RateEntity> root, final CriteriaQuery<?> query,
			final CriteriaBuilder criteriaBuilder) {
		return criteriaBuilder.and(
				criteriaBuilder.lessThanOrEqualTo(root.<LocalDate>get("effectiveStartDate"), this.effectiveDate),
				criteriaBuilder.greaterThanOrEqualTo(root.<LocalDate>get("effectiveEndDate"), this.effectiveDate));
	}

}
