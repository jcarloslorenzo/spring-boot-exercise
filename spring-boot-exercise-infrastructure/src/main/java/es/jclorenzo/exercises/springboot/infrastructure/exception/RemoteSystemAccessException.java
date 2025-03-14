package es.jclorenzo.exercises.springboot.infrastructure.exception;

/**
 * The Class RemoteSystemAccessException.
 */
public class RemoteSystemAccessException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8309944645469479987L;

	/**
	 * Instantiates a new remote system access exception.
	 *
	 * @param message the message
	 */
	public RemoteSystemAccessException(final String message) {
		super(message);
	}

	/**
	 * Instantiates a new remote system access exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public RemoteSystemAccessException(final String message , final Throwable cause) {
		super(message, cause);
	}

}
