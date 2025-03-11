package es.jclorenzo.exercises.springboot.repository.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The Class RateEntity.
 */
@Data
@Entity
@AllArgsConstructor
@Table(name = "t_rates", schema = "public")
public class RateEntity {

	/** Identificador único de la tarifa. */
	@Id
	@Column(name = "id", nullable = false, insertable = false, updatable = false)
	@GeneratedValue
	private Integer id;

	/** Identificador único de la marca. */
	@Column(name = "brand_id", nullable = false, insertable = true, updatable = true)
	private Integer brandId;

	/** Identificador único del producto. */
	@Column(name = "product_id", nullable = false, insertable = true, updatable = true)
	private Integer productId;

	/** Fecha de inicio de aplicación de la tarifa. */
	@Column(name = "start_date", nullable = false, insertable = true, updatable = true)
	private LocalDate startDate;

	/** Fecha de fin de aplicación de la tarifa. */
	@Column(name = "end_date", nullable = false, insertable = true, updatable = true)
	private LocalDate endDate;

	/** Precio del producto sin decimales. */
	@Column(name = "price", nullable = false, insertable = true, updatable = true)
	private Integer price;

	/** ID de la moneda en que está representado el precio. */
	@Column(name = "currency_code", nullable = false, insertable = true, updatable = true)
	private String currencyCode;

}
