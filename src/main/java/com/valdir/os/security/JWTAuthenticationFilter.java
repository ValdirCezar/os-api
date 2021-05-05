package com.valdir.os.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.management.RuntimeErrorException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valdir.os.dtos.CredenciaisDTO;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	private AuthenticationManager authenticationManager;

    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    
	/*
	 * Method to attempt authentication
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		LOG.info("JWTAuthenticationFilter - REQUISIÇÃO CHEGOU NO FILTRO DE AUTENTICAÇÃO");
		
		/*
		 * try authentication and case we have an error will send a new exception
		 */
		try {
			LOG.info("JWTAuthenticationFilter - TRANSFORMANDO DADOS DA REQUISIÇÃO EM CredenciaisDTO");
			CredenciaisDTO creds = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);

			LOG.info("JWTAuthenticationFilter - GERANDO TOKEN DE AUTENTICAÇÃO");
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					creds.getCpf(), creds.getSenha(), new ArrayList<>());

			LOG.info("JWTAuthenticationFilter - TENTANDO AUTENTICAR");
			Authentication auth = authenticationManager.authenticate(authenticationToken);
			
			LOG.info("JWTAuthenticationFilter - REQUISIÇÃO PASSOU NO FILTRO DE AUTENTICAÇÃO");
			return auth;
		} catch (Error | IOException e) {
			LOG.info("JWTAuthenticationFilter - FALHA NA AUTENTICAÇÃO");
			throw new RuntimeErrorException((Error) e);
		}
	} 

	/*
	 * Case successful authentication the object auth will be sended to method
	 * seccessfulAuthentication where we will take cpf and generate a token
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		LOG.info("JWTAuthenticationFilter - REALIZANDO PARSE PARA UserSS");
		String cpf = ((UserSS) authResult.getPrincipal()).getUsername();

		String token = jwtUtil.generateToken(cpf);
		
		LOG.info("JWTAuthenticationFilter - ADICIONANDO TOKEN NO HEADER DA RESPOSTA");
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
	}	

	@Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOG.info("JWTAuthenticationFilter - FALHA NA AUTENTICAÇÃO");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().print(authException.getLocalizedMessage());
        response.getWriter().flush();
    }
}
