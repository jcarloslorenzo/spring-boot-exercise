package es.jclorenzo.exercises.springboot.infrastructure.persistence.test.utils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

/**
 * The Class PostgresTestContainer.
 */
public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

	/** The Constant IMAGE_VERSION. */
	private static final String IMAGE_VERSION = "postgres:17.4";

	/** The container. */
	private static PostgresTestContainer postgreContainer;

	/**
	 * Instantiates a new postgres test container.
	 */
	private PostgresTestContainer() {
		super(PostgresTestContainer.IMAGE_VERSION);
	}

	/**
	 * Gets the single instance of PostgresTestContainer.
	 *
	 * @return single instance of PostgresTestContainer
	 */
	public static PostgresTestContainer getInstance() {
		if (PostgresTestContainer.postgreContainer == null) {
			PostgresTestContainer.postgreContainer = new PostgresTestContainer();
			PostgresTestContainer.postgreContainer.withInitScript("init.sql");
			PostgresTestContainer.postgreContainer.waitingFor(new LogMessageWaitStrategy()
					.withRegEx(".*database system is ready to accept connections.*\\s")
					.withTimes(2)
					.withStartupTimeout(Duration.of(60, ChronoUnit.SECONDS)));
			PostgresTestContainer.postgreContainer.start();
		}
		return PostgresTestContainer.postgreContainer;
	}

}