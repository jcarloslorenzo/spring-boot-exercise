package es.jclorenzo.exercises.springboot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.Generated;

/**
 * The Class Application.
 */
@SpringBootApplication(scanBasePackages = "es.jclorenzo.exercises.springboot.*.config")
public class Application {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	@Generated
	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
