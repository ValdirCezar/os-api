package com.valdir.os.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * Add injection of this Env to read a active profiles and free access to the
	 * h2-console om browser
	 */
	@Autowired
	private Environment environment;

	private static final String[] PUBLIC_MATCHERS = { "/h2-console/**" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		/*
		 * This condition will verify if the active profile contais the test profile to
		 * free access to h2-console om browser
		 */
		if (Arrays.asList(this.environment.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}

		// Enabling cors and disabling csrf
		http.cors().and().csrf().disable();

		http.authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll().anyRequest().authenticated();

		// Ensuring that our application will not create a user session
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	/*
	 * By default the cors is bloqued on API when spring security is added and to
	 * disable cors we need to make it explicit
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

	/*
	 * My system will have available a @Bean to encript passwords that i will be
	 * able to inject in others classes from system
	 */
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
