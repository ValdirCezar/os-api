package com.valdir.os.resources;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valdir.os.security.JWTUtil;
import com.valdir.os.security.UserSS;
import com.valdir.os.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthResource.class);

	@Autowired
	private JWTUtil jwtUtil;

	@PostMapping(value = "/refresh_token")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		LOG.info("AuthResource - ATUALIZANDO TOKEN");
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		LOG.info("AuthResource - NOVO TOKEN FOI GERADO");
		return ResponseEntity.noContent().build();
	}

}
