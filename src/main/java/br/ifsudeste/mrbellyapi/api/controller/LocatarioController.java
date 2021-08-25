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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/locatarios")
@RequiredArgsConstructor
@Api("API de Locatários")
public class LocatarioController {

	private final LocatarioService service;

	@GetMapping()
	@ApiOperation("Obter todos os locatários")
	public ResponseEntity get() {
		List<Locatario> locatarios = service.getLocatarios();
		return ResponseEntity.ok(locatarios.stream().map(LocatarioDTO::create).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	@ApiOperation("Obter detalhes de um locatário")
	@ApiResponses({ 
		@ApiResponse(code = 200, message = "Locatário encontrado"),
		@ApiResponse(code = 404, message = "Locatário não encontrado")
	})
	public ResponseEntity get(@PathVariable("id") @ApiParam("Id do locatário") Long id) {
		Optional<Locatario> locatario = service.getLocatarioById(id);
		if (!locatario.isPresent()) {
			return new ResponseEntity("Locatário não encontrado", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(locatario.map(LocatarioDTO::create));
	}

	@PostMapping()
	@ApiOperation("Salva um novo locatário")
	@ApiResponses({
        @ApiResponse(code = 201, message = "Locatário salvo com sucesso"),
        @ApiResponse(code = 400, message = "Erro ao salvar o locatário")
	})
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
	@ApiOperation("Salva um novo locatário")
	public ResponseEntity atualizar(@PathVariable("id") @ApiParam("Id do locatário") Long id, LocatarioDTO dto) {
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
	@ApiOperation("Excluir um locatário")
	public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do locatário") Long id) {
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
