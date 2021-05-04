package com.valdir.os.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.valdir.os.domain.Pessoa;
import com.valdir.os.repositories.PessoaRepository;
import com.valdir.os.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private PessoaRepository repository;

	@Override
	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
		Pessoa obj = repository.findByCPF(cpf);
		
		if(obj == null) {
			throw new UsernameNotFoundException(cpf);
		}
		
		return new UserSS(obj.getId(), obj.getCpf(), obj.getSenha(), obj.getPerfis());
	}
}
