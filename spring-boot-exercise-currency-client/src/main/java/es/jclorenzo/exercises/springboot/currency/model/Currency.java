package es.jclorenzo.exercises.springboot.currency.model;

/**
 * Currency.
 *
 * @param symbol the symbol
 * @param code the code
 * @param decimals the decimals
 */

public record Currency(String symbol, String code, Integer decimals) {

}
