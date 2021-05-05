package com.valdir.os.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.valdir.os.domain.Tecnico;
import com.valdir.os.dtos.TecnicoDTO;
import com.valdir.os.services.TecnicoService;

@RestController
@RequestMapping(value = "/tecnicos")
public class TecnicoResource {

	Logger log = LoggerFactory.getLogger(TecnicoService.class);
	
	@Autowired
	private TecnicoService service;

	/*
	 * Busca pelo ID
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> findById(@PathVariable Integer id) {
		log.info("RESOURCE - BUSCANDO TÉCNICO POR ID");
		TecnicoDTO objDTO = new TecnicoDTO(service.findById(id));
		log.info("RESOURCE - RETORNANDO RESPOSTA PARA REQUISIÇÃO");
		return ResponseEntity.ok().body(objDTO);
	}

	/*
	 * Lista todos objetos do tipo Tecnico na base
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<TecnicoDTO>> findAll() {
		log.info("RESOURCE - BUSCANDO TODOS OD TÉCNICOS");
		List<TecnicoDTO> listDTO = service.findAll().stream().map(obj -> new TecnicoDTO(obj))
				.collect(Collectors.toList());

		log.info("RESOURCE - RETORNANDO RESPOSTA PARA REQUISIÇÃO");
		return ResponseEntity.ok().body(listDTO);
	}

	/*
	 * Cria um novo Tecnico
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<TecnicoDTO> create(@Valid @RequestBody TecnicoDTO objDTO) {
		log.info("RESOURCE - CRIANDO NOVO TÉCNICO");
		Tecnico newObj = service.create(objDTO);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();

		log.info("RESOURCE - RETORNANDO RESPOSTA PARA REQUISIÇÃO");
		return ResponseEntity.created(uri).build();
	}

	/*
	 * Atualiza um Tecnico
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> update(@PathVariable Integer id, @Valid @RequestBody TecnicoDTO objDTO) {
		log.info("RESOURCE - ATUALIZANDO TÉCNICO");
		TecnicoDTO newObj = new TecnicoDTO(service.update(id, objDTO));
		log.info("RESOURCE - RETORNANDO RESPOSTA PARA REQUISIÇÃO");
		return ResponseEntity.ok().body(newObj);
	}

	/*
	 * Delete Tecnico
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		log.info("RESOURCE - DELETANDO TÉCNICO");
		service.delete(id);
		log.info("RESOURCE - RETORNANDO RESPOSTA PARA REQUISIÇÃO");
		return ResponseEntity.noContent().build();
	}

}
