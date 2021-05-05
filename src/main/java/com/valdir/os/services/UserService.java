package com.valdir.os.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.valdir.os.resources.AuthResource;
import com.valdir.os.security.UserSS;

public class UserService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthResource.class);
	
	public static UserSS authenticated() {
		try {
			LOG.info("UserService - ANALIZANDO SE O USUÁRIO ESTÁ AUTENTICADO");
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
