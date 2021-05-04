package com.valdir.os.dtos;

import java.io.Serializable;

public class CredenciaisDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String cpf;
	private String senha;

	public CredenciaisDTO(String cpf, String senha) {
		super();
		this.cpf = cpf;
		this.senha = senha;
	}

	public CredenciaisDTO() {
		super();
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
