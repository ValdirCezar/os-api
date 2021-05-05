package com.valdir.os.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.valdir.os.domain.Pessoa;
import com.valdir.os.domain.Tecnico;
import com.valdir.os.domain.enuns.Perfil;
import com.valdir.os.dtos.TecnicoDTO;
import com.valdir.os.repositories.PessoaRepository;
import com.valdir.os.repositories.TecnicoRepository;
import com.valdir.os.services.exceptions.DataIntegratyViolationException;
import com.valdir.os.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {

	Logger log = LoggerFactory.getLogger(TecnicoService.class);

	@Autowired
	private TecnicoRepository repository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	/*
	 * Busca Tecnico pelo ID
	 */
	public Tecnico findById(Integer id) {
		log.info("SERVICE - BUSCANDO TÉCNICO POR ID");
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Tecnico.class.getName()));
	}

	/*
	 * Busca todos os Tecnicos da base de dados
	 */
	public List<Tecnico> findAll() {
		log.info("SERVICE - BUSCANDO TODOS OD TÉCNICOS");
		return repository.findAll();
	}

	/*
	 * Cria um Tecnico
	 */
	public Tecnico create(TecnicoDTO objDTO) {
		log.info("SERVICE - CRIANDO NOVO TÉCNICO");
		if (findByCPF(objDTO) != null) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}

		Tecnico newTec = new Tecnico(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone(),
				encoder.encode(objDTO.getSenha()));

		if (objDTO.getPerfis().contains(Perfil.ADMIN)) {
			newTec.addPerfil(Perfil.ADMIN);
		}

		return repository.save(newTec);
	}

	/*
	 * Atualiza um Tecnico
	 */
	public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
		log.info("SERVICE - ATUALIZANDO TÉCNICO");
		Tecnico oldObj = findById(id);

		if (findByCPF(objDTO) != null && findByCPF(objDTO).getId() != id) {
			throw new DataIntegratyViolationException("CPF já cadastrado na base de dados!");
		}

		oldObj.setNome(objDTO.getNome());
		oldObj.setCpf(objDTO.getCpf());
		oldObj.setTelefone(objDTO.getTelefone());
		if (objDTO.getPerfis().contains(Perfil.ADMIN)) {
			oldObj.addPerfil(Perfil.ADMIN);
		}
		return repository.save(oldObj);
	}

	/*
	 * Deleta um Tecnico pelo ID
	 */
	public void delete(Integer id) {
		log.info("SERVICE - DELETANDO TÉCNICO");
		Tecnico obj = findById(id);

		if (obj.getList().size() > 0) {
			throw new DataIntegratyViolationException("Técnico possui Ordens de Serviço, não pode ser deletado!");
		}

		repository.deleteById(id);
	}

	/*
	 * Busca Tecnico pelo CPF
	 */
	private Pessoa findByCPF(TecnicoDTO objDTO) {
		log.info("SERVICE - ANALIZANDO SE O CPF ESTÁ CADASTRADO NO BANCO");
		Pessoa obj = pessoaRepository.findByCPF(objDTO.getCpf());

		if (obj != null) {
			return obj;
		}
		return null;
	}

}
