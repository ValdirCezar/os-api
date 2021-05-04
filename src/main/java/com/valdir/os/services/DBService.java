package com.valdir.os.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.valdir.os.domain.Cliente;
import com.valdir.os.domain.OS;
import com.valdir.os.domain.Tecnico;
import com.valdir.os.domain.enuns.Perfil;
import com.valdir.os.domain.enuns.Prioridade;
import com.valdir.os.domain.enuns.Status;
import com.valdir.os.repositories.ClienteRepository;
import com.valdir.os.repositories.OSRepository;
import com.valdir.os.repositories.TecnicoRepository;

@Service
public class DBService {

	@Autowired
	private TecnicoRepository tecnicoRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private OSRepository osRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;

	public void instanciaDB() {
		Tecnico t1 = new Tecnico(null, "Valdir Cezar", "144.785.300-84", "(88) 98888-8888", encoder.encode("123"));
		t1.addPerfil(Perfil.ADMIN);
		Tecnico t2 = new Tecnico(null, "Linus Torvalds", "641.760.040-88", "(88) 94545-4545", encoder.encode("1234"));
		Tecnico t3 = new Tecnico(null, "Alan Turing", "332.040.820-83", "(88) 96345-9874", encoder.encode("1235"));
		Tecnico t4 = new Tecnico(null, "Richard Stallman", "756.192.280-96", "(88) 98745-8542", encoder.encode("1236"));
		Tecnico t5 = new Tecnico(null, "Tim Berners-Lee", "926.076.200-66", "(88) 98545-3685", encoder.encode("1235"));

		Cliente c1 = new Cliente(null, "Betina Campos", "598.508.200-80", "(88) 98888-7777", encoder.encode("1233"));
		Cliente c2 = new Cliente(null, "Galileu Galilei", "089.637.320-70", "(88) 97854-6985", encoder.encode("1233"));
		Cliente c3 = new Cliente(null, "Isaac Newton", "422.876.280-88", "(88) 95555-6541", encoder.encode("1253"));
		Cliente c4 = new Cliente(null, "Marie Curie", "420.724.490-57", "(88) 96666-8523", encoder.encode("1523"));
		Cliente c5 = new Cliente(null, "Albert Einstein", "047.166.710-20", "(88) 98755-4412", encoder.encode("1253"));

		OS os1 = new OS(null, Prioridade.ALTA, "Trocar fonte do notebook", Status.ANDAMENTO, t1, c1);
		OS os2 = new OS(null, Prioridade.BAIXA, "Trocar placa mãe", Status.ANDAMENTO, t2, c2);
		OS os3 = new OS(null, Prioridade.ALTA, "Formatar para linux", Status.ANDAMENTO, t3, c3);
		OS os4 = new OS(null, Prioridade.MEDIA, "Ativar antivirus", Status.ANDAMENTO, t4, c4);
		OS os5 = new OS(null, Prioridade.MEDIA, "Criar sistema full stack", Status.ANDAMENTO, t5, c5);
		OS os6 = new OS(null, Prioridade.BAIXA, "Trocar pasta térmica", Status.ANDAMENTO, t1, c1);

		t1.getList().add(os1);
		t1.getList().add(os6);
		t2.getList().add(os2);
		t3.getList().add(os3);
		t4.getList().add(os4);
		t5.getList().add(os5);

		c1.getList().add(os1);
		c2.getList().add(os2);
		c3.getList().add(os3);
		c4.getList().add(os4);
		c5.getList().add(os5);
		c1.getList().add(os6);

		tecnicoRepository.saveAll(Arrays.asList(t1, t2, t3, t4, t5));
		clienteRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5));
		osRepository.saveAll(Arrays.asList(os1, os2, os3, os4, os5, os6));
	}
}
