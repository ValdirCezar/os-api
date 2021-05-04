package com.valdir.os.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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

	public boolean tokenValido(String token) {
		// Claims stores token claims
		Claims claims = getClaimsToken(token);

		if (claims != null) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			if (username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}

		return false;
	}

	/*
	 * This methoid will get the claims from token
	 */
	private Claims getClaimsToken(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}

	public String getUsername(String token) {
		Claims claims = getClaimsToken(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}
}
