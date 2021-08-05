package br.ifsudeste.mrbellyapi.api.controller;

import br.ifsudeste.mrbellyapi.api.dto.EnderecoDTO;
import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Endereco;
import br.ifsudeste.mrbellyapi.service.EnderecoService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/enderecos")
@RequiredArgsConstructor
public class EnderecoController {
	private final EnderecoService service;

	@GetMapping()
	public ResponseEntity get() {
		List<Endereco> enderecos = service.getEnderecos();
		return ResponseEntity.ok(enderecos.stream().map(EnderecoDTO::create));
	}

	@GetMapping("/{id}")
	public ResponseEntity get(@PathVariable("id") Long id) {
		Optional<Endereco> endereco = service.getEnderecoById(id);
		if (!endereco.isPresent()) {
			return new ResponseEntity("Endereço não encontrado", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(endereco.map(EnderecoDTO::create));
	}

	public Endereco converter(EnderecoDTO dto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(dto, Endereco.class);
	}
}
