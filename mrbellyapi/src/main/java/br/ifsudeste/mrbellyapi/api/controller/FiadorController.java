package br.ifsudeste.mrbellyapi.api.controller;

import br.ifsudeste.mrbellyapi.api.dto.FiadorDTO;
import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Endereco;
import br.ifsudeste.mrbellyapi.model.entity.Fiador;
import br.ifsudeste.mrbellyapi.service.EnderecoService;
import br.ifsudeste.mrbellyapi.service.FiadorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/fiadores")
@RequiredArgsConstructor
public class FiadorController {
	private final FiadorService service;
	private final EnderecoService enderecoService;

	@GetMapping()
	public ResponseEntity get() {
		List<Fiador> fiadores = service.getFiadores();
		return ResponseEntity.ok(fiadores.stream().map(FiadorDTO::create));
	}

	@GetMapping("/{id}")
	public ResponseEntity get(@PathVariable("id") Long id) {
		Optional<Fiador> fiador = service.getFiadorById(id);
		if (!fiador.isPresent()) {
			return new ResponseEntity("Fiador não encontrado", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(fiador.map(FiadorDTO::create));
	}

	@PostMapping()
	public ResponseEntity post(FiadorDTO dto) {
		try {
			Fiador fiador = converter(dto);
			fiador = service.salvar(fiador);
			return new ResponseEntity(fiador, HttpStatus.CREATED);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity ataulizar(@PathVariable("id") Long id, FiadorDTO dto) {
		if (!service.getFiadorById(id).isPresent()) {
			return new ResponseEntity("Fiador não encontrado", HttpStatus.NOT_FOUND);
		}

		try {
			Fiador fiador = converter(dto);
			fiador.setId(id);
			service.salvar(fiador);
			return ResponseEntity.ok(fiador);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity excluir(@PathVariable("id") Long id) {
		Optional<Fiador> fiador = service.getFiadorById(id);
		if (!fiador.isPresent()) {
			return new ResponseEntity("Fiador não encontrado", HttpStatus.NOT_FOUND);
		}
		try {
			service.excluir(fiador.get());
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public Fiador converter(FiadorDTO dto) {
		ModelMapper modelMapper = new ModelMapper();

		Fiador fiador = modelMapper.map(dto, Fiador.class);
		Endereco endereco = modelMapper.map(dto, Endereco.class);

		fiador.setEndereco(endereco);

		return fiador;
	}

}
