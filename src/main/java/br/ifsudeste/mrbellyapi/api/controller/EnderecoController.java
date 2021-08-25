package br.ifsudeste.mrbellyapi.api.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsudeste.mrbellyapi.api.dto.EnderecoDTO;
import br.ifsudeste.mrbellyapi.model.entity.Endereco;
import br.ifsudeste.mrbellyapi.service.EnderecoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/enderecos")
@RequiredArgsConstructor
@Api("API de Endereços")
public class EnderecoController {
	private final EnderecoService service;

	@GetMapping()
	@ApiOperation("Obter todos os endereços")
	public ResponseEntity get() {
		List<Endereco> enderecos = service.getEnderecos();
		return ResponseEntity.ok(enderecos.stream().map(EnderecoDTO::create));
	}

	@GetMapping("/{id}")
	@ApiOperation("Obter detalhes de um endereço")
	@ApiResponses({ 
		@ApiResponse(code = 200, message = "Endereço encontrado"),
		@ApiResponse(code = 404, message = "Endereço não encontrado")
	})
	public ResponseEntity get(@PathVariable("id") @ApiParam("Id do endereço") Long id) {
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
