package es.jclorenzo.exercises.springboot.infrastructure.adapter.currency.dto;

/**
 * Currency.
 *
 * @param symbol   the symbol
 * @param code     the code
 * @param decimals the decimals
 */

public record RemoteCurrency(String symbol, String code, Integer decimals) {

}
