package com.valdir.os.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.valdir.os.domain.Pessoa;
import com.valdir.os.repositories.PessoaRepository;
import com.valdir.os.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private PessoaRepository repository;

	@Override
	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
		logger.info("UserDetailsServiceImpl - CHEGOU NO USER DETAILS SERVICE");
		Pessoa obj = repository.findByCPF(cpf);

		if (obj == null) {
			logger.info("UserDetailsServiceImpl - USUÁRIO OU SENHA INVÁLIDOS");
			throw new UsernameNotFoundException(cpf);
		}

		logger.info("UserDetailsServiceImpl - PASSOU NO USER DETAILS SERVICE");
		UserSS user = new UserSS(obj.getId(), obj.getCpf(), obj.getSenha(), obj.getPerfis());
		return user;
	}
}
