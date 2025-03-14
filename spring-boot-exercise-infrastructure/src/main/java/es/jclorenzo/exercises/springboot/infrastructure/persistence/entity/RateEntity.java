package es.jclorenzo.exercises.springboot.infrastructure.persistence.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class RateEntity.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_rates", schema = "public")
public class RateEntity {

	/** Identificador único de la tarifa. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, insertable = false, updatable = false)
	private Integer id;

	/** Identificador único de la marca. */
	@Column(name = "brand_id", nullable = false, insertable = true, updatable = true)
	private Integer brandId;

	/** Identificador único del producto. */
	@Column(name = "product_id", nullable = false, insertable = true, updatable = true)
	private Integer productId;

	/** Fecha de inicio de aplicación de la tarifa. */
	@Column(name = "start_date", nullable = false, insertable = true, updatable = true)
	private LocalDate effectiveStartDate;

	/** Fecha de fin de aplicación de la tarifa. */
	@Column(name = "end_date", nullable = false, insertable = true, updatable = true)
	private LocalDate effectiveEndDate;

	/** Precio del producto sin decimales. */
	@Column(name = "price", nullable = false, insertable = true, updatable = true)
	private Integer price;

	/** ID de la moneda en que está representado el precio. */
	@Column(name = "currency_code", nullable = false, insertable = true, updatable = true)
	private String currencyCode;

}
