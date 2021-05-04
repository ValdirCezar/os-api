package com.valdir.os.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	/*
	 * Method to generate a token
	 */
	public String generateToken(String cpf) {

		/*
		 * We will use the jwt now
		 */
		return Jwts.builder()
				// Username
				.setSubject(cpf)
				// Time to expiration from token
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				// To say how I'm going to sign my token
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
	}
}
