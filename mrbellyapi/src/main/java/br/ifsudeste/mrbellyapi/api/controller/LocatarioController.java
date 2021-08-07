package br.ifsudeste.mrbellyapi.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsudeste.mrbellyapi.api.dto.LocatarioDTO;
import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Locatario;
import br.ifsudeste.mrbellyapi.service.LocatarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/locatarios")
@RequiredArgsConstructor
public class LocatarioController {

	private final LocatarioService service;

	@GetMapping()
	public ResponseEntity get() {
		List<Locatario> locatarios = service.getLocatarios();
		return ResponseEntity.ok(locatarios.stream().map(LocatarioDTO::create).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public ResponseEntity get(@PathVariable("id") Long id) {
		Optional<Locatario> locatario = service.getLocatarioById(id);
		if (!locatario.isPresent()) {
			return new ResponseEntity("Locatário não encontrado", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(locatario.map(LocatarioDTO::create));
	}

	@PostMapping()
	public ResponseEntity post(LocatarioDTO dto) {
		try {
			Locatario locatario = converter(dto);
			locatario = service.salvar(locatario);
			return new ResponseEntity(locatario, HttpStatus.CREATED);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, LocatarioDTO dto) {
		if (!service.getLocatarioById(id).isPresent()) {
			return new ResponseEntity("Locatário não encontrado", HttpStatus.NOT_FOUND);
		}
		try {
			Locatario locatario = converter(dto);
			service.salvar(locatario);
			return new ResponseEntity(locatario, HttpStatus.CREATED);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity excluir(@PathVariable("id") Long id) {
		Optional<Locatario> locatario = service.getLocatarioById(id);
		if (!locatario.isPresent()) {
			return new ResponseEntity("Locatário não encontrado", HttpStatus.NOT_FOUND);
		}
		try {
			service.excluir(locatario.get());
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public Locatario converter(LocatarioDTO dto) {
		ModelMapper modelMapper = new ModelMapper();
		Locatario locatario = modelMapper.map(dto, Locatario.class);
		return locatario;
	}
}
