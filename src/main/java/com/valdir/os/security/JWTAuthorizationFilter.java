package com.valdir.os.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private static final Logger LOG = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

	private JWTUtil jwtUtil;

	/*
	 * Need from UserDetailsService to find a user by CPF
	 */
	private UserDetailsService userDetailsService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
			UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		LOG.info("JWTAuthorizationFilter - ENTROU NO FILTRO INTERNO");
		
		/*
		 * First I need take Authorization value that has on header from request
		 */
		String header = request.getHeader("Authorization");

		/*
		 * Method to free access to user that was trying access the endpoint
		 */
		if (header != null && header.startsWith("Bearer ")) {
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		LOG.info("JWTAuthorizationFilter - OBTENDO AUTENTICAÇÃO");
		if(jwtUtil.tokenValido(token)) {
			String username = jwtUtil.getUsername(token);
			UserDetails user = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}

		LOG.info("JWTAuthorizationFilter - TOKEN INVÁLIDO. FALHA NA AUTENTICAÇÃO");
		return null;
	}

}
