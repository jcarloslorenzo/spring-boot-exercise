package es.jclorenzo.exercises.springboot.api.controller.advice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import es.jclorenzo.exercises.springboot.currency.exception.RemoteSystemAccessException;
import es.jclorenzo.exercises.springboot.service.exception.DateRangeOverlapException;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class RatesControllerAdvice.
 */
@Slf4j
@RestControllerAdvice
public class RatesControllerAdvice
		extends ResponseEntityExceptionHandler {

	/**
	 * Handle no such element exception.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the response entity
	 */
	// Manejo específico para NoSuchElementException: devuelve 404 (Not Found)
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Object> handleNoSuchElementException(final NoSuchElementException ex,
			final WebRequest request) {
		return this.generateResponseEntity(ex, HttpStatus.NOT_FOUND, "Not Found", request);
	}

	/**
	 * Handle access denied exception.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the response entity
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException ex,
			final WebRequest request) {
		return this.generateResponseEntity(ex, HttpStatus.NOT_FOUND, "Not Found", request);
	}

	/**
	 * Handle date range overlap exception.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the responde entity
	 */
	@ExceptionHandler(DateRangeOverlapException.class)
	public ResponseEntity<Object> handleDateRangeOverlapException(final DateRangeOverlapException ex,
			final WebRequest request) {
		return this.generateResponseEntity(ex, HttpStatus.CONFLICT, "Conflicting data", request);
	}

	/**
	 * Handle illegal argument exception.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the response entity
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex,
			final WebRequest request) {
		return this.generateResponseEntity(ex, HttpStatus.BAD_REQUEST, "Bad request", request);
	}

	/**
	 * Handle date range overlap exception.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the response entity
	 */
	@ExceptionHandler(RemoteSystemAccessException.class)
	public ResponseEntity<Object> handleRemoteSystemAccessException(final RemoteSystemAccessException ex,
			final WebRequest request) {
		return this.generateResponseEntity(ex, HttpStatus.FAILED_DEPENDENCY, "Remote system error", request);
	}

	/**
	 * Generate body.
	 *
	 * @param ex       the ex
	 * @param status   the status
	 * @param errorMsg the error msg
	 * @param request  the request
	 * @return the map
	 */
	private ResponseEntity<Object> generateResponseEntity(
			final Exception ex, final HttpStatus status,
			final String errorMsg, final WebRequest request) {
		RatesControllerAdvice.log.debug("Exception ", ex);

		final Map<String, Object> body = Map.of(
				"timestamp", LocalDateTime.now(),
				"status", status.value(),
				"error", errorMsg,
				"message", ex.getMessage(),
				"path", request.getDescription(false).replace("uri=", ""));

		return super.handleExceptionInternal(ex, body, null, status, request);
	}

}
