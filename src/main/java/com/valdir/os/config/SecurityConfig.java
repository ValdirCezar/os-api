package com.valdir.os.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.valdir.os.security.JWTAuthenticationFilter;
import com.valdir.os.security.JWTAuthorizationFilter;
import com.valdir.os.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * Add injection of this Env to read a active profiles and free access to the
	 * h2-console om browser
	 */
	@Autowired
	private Environment environment;

	/*
	 * Using our UserDetailsService on authentication to see the informations from
	 * user and find a user by CPF
	 */
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JWTUtil jwtUtil;

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
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	/*
	 * This method will say to Spring Security who is my UserDateilsService and my
	 * password encoder
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
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
