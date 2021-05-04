package com.valdir.os.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Cliente extends Pessoa implements Serializable {
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "cliente")
	private List<OS> list = new ArrayList<>();

	public Cliente() {
		super();
	}

	public Cliente(Integer id, String nome, String cpf, String telefone, String senha) {
		super(id, nome, cpf, telefone, senha);
	}

	public List<OS> getList() {
		return list;
	}

	public void setList(List<OS> list) {
		this.list = list;
	}

}
