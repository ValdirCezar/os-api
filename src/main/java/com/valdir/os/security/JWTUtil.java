package com.valdir.os.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(JWTUtil.class);

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	/*
	 * Method to generate a token
	 */
	public String generateToken(String cpf) {
		LOG.info("JWTUtil - GERANDO TOKEN");

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
		LOG.info("JWTUtil - VERIFICANDO SE TOKEN É VÁLIDO");
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
		LOG.info("JWTUtil - OBTENDO CLAIMS DO TOKEN");
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}

	public String getUsername(String token) {
		LOG.info("JWTUtil - OBTENDO USERNAME DO TOKEN");
		Claims claims = getClaimsToken(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}
}
