package br.ifsudeste.mrbellyapi.api.controller;

import java.util.List;
import java.util.Optional;

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

import br.ifsudeste.mrbellyapi.api.dto.FiadorDTO;
import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Endereco;
import br.ifsudeste.mrbellyapi.model.entity.Fiador;
import br.ifsudeste.mrbellyapi.service.EnderecoService;
import br.ifsudeste.mrbellyapi.service.FiadorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/fiadores")
@RequiredArgsConstructor
@Api("API de Fiadores")
public class FiadorController {
	private final FiadorService service;
	private final EnderecoService enderecoService;

	@GetMapping()
	@ApiOperation("Obter todos os fiadores")
	public ResponseEntity get() {
		List<Fiador> fiadores = service.getFiadores();
		return ResponseEntity.ok(fiadores.stream().map(FiadorDTO::create));
	}

	@GetMapping("/{id}")
	@ApiOperation("Obter detalhes de um fiador")
	@ApiResponses({ 
		@ApiResponse(code = 200, message = "Fiador encontrado"),
		@ApiResponse(code = 404, message = "Fiador não encontrado")
	})
	public ResponseEntity get(@PathVariable("id") @ApiParam("Id do fiador") Long id) {
		Optional<Fiador> fiador = service.getFiadorById(id);
		if (!fiador.isPresent()) {
			return new ResponseEntity("Fiador não encontrado", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(fiador.map(FiadorDTO::create));
	}

	@PostMapping()
	@ApiOperation("Salva um novo fiador")
	@ApiResponses({
        @ApiResponse(code = 201, message = "Fiador salvo com sucesso"),
        @ApiResponse(code = 400, message = "Erro ao salvar o fiador")
	})
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
	@ApiOperation("Modifica dados de um fiador ")
	public ResponseEntity ataulizar(@PathVariable("id") @ApiParam("Id do fiador") Long id, FiadorDTO dto) {
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
	@ApiOperation("Excluir um fiador")
	public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do fiador") Long id) {
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
