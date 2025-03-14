package es.jclorenzo.exercises.springboot.application.exception;

/**
 * The Class DateRangeOverlapException.
 */
public class DateRangeOverlapException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3126450508008136005L;

	/**
	 * Instantiates a new date range overlap exception.
	 *
	 * @param message the message
	 */
	public DateRangeOverlapException(final String message) {
		super(message);
	}

}
