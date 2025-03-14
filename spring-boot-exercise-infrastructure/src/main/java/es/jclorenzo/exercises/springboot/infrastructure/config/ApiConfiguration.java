package es.jclorenzo.exercises.springboot.infrastructure.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * The Class ModuleConfiguration.
 */
@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = { "es.jclorenzo.exercises.springboot.infrastructure.api" })
public class ApiConfiguration {

	/** The context path. */
	@Value("${server.servlet.context-path:/test}")
	private String contextPath;

	/** The server port. */
	@Value("${server.port:9090}")
	private String serverPort;

	/** The ssl enabled. */
	@Value("${server.ssl.enabled:false}")
	private boolean sslEnabled;

	/**
	 * Configure a in-memory user.
	 *
	 * @return the user details service
	 */
	@Bean
	UserDetailsService users() {
		final UserDetails user = User.builder()
				.username("user")
				.password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
				.roles("USER")
				.build();
		return new InMemoryUserDetailsManager(user);
	}

	/**
	 * Filter chain.
	 *
	 * @param httpSecurity the http security
	 * @return the security filter chain
	 * @throws Exception the exception
	 */
	@Bean
	SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.csrf(CsrfConfigurer::disable)
				.authorizeHttpRequests(auth -> {
					auth.requestMatchers(HttpMethod.POST, "/v1/rates/**").authenticated();
					auth.requestMatchers("/v1/rates/**").authenticated();
					auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api-docs.yaml").permitAll();
					auth.anyRequest().denyAll();
				}).httpBasic(Customizer.withDefaults());
		return httpSecurity.build();
	}

	/**
	 * Custom open API.
	 *
	 * @return the open API
	 */
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.security(List.of(new SecurityRequirement().addList("BasicAuth")))
				.components(
						new Components().addSecuritySchemes(
								"BasicAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")));
	}
}