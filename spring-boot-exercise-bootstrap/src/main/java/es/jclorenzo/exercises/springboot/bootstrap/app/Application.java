package es.jclorenzo.exercises.springboot.bootstrap.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import lombok.Generated;

/**
 * The Class Application.
 */
@EnableCaching
@SpringBootApplication(scanBasePackages = {
		"es.jclorenzo.exercises.springboot.infrastructure.config",
		"es.jclorenzo.exercises.springboot.application.config"
})
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
