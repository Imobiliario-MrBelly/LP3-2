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

import br.ifsudeste.mrbellyapi.api.dto.ImovelDTO;
import br.ifsudeste.mrbellyapi.api.dto.LocadorDTO;
import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Endereco;
import br.ifsudeste.mrbellyapi.model.entity.Imovel;
import br.ifsudeste.mrbellyapi.model.entity.Locador;
import br.ifsudeste.mrbellyapi.service.EnderecoService;
import br.ifsudeste.mrbellyapi.service.ImovelService;
import br.ifsudeste.mrbellyapi.service.LocadorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/locadores")
@RequiredArgsConstructor
@Api("API de Locadores")
public class LocadorController {
	private final LocadorService service;
	private final EnderecoService enderecoService;
	private final ImovelService imovelService;

	@GetMapping()
	@ApiOperation("Obter todos os locadores")
	public ResponseEntity get() {
		List<Locador> locadores = service.getLocadores();
		return ResponseEntity.ok(locadores.stream().map(LocadorDTO::create));
	}

	@GetMapping("/{id}")
	@ApiOperation("Obter detalhes de um locador")
	@ApiResponses({ 
		@ApiResponse(code = 200, message = "Locador encontrado"),
		@ApiResponse(code = 404, message = "Locador não encontrado")
	})
	public ResponseEntity get(@PathVariable("id") @ApiParam("Id do locador") Long id) {
		Optional<Locador> locador = service.getLocadorById(id);
		if (!locador.isPresent()) {
			return new ResponseEntity("Locador não encontrado", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(locador.map(LocadorDTO::create));
	}

	@GetMapping("/{id}/imoveis")
	@ApiOperation("Mostra todos os imóveis que um locador possui")
	public ResponseEntity getImoveis(@PathVariable("id") @ApiParam("Id do locador") Long id) {
		Optional<Locador> locador = service.getLocadorById(id);
		if (!locador.isPresent()) {
			return new ResponseEntity("Locador não encontrado", HttpStatus.NOT_FOUND);
		}
		List<Imovel> imoveis = imovelService.getImovelByLocador(locador);
		return ResponseEntity.ok(imoveis.stream().map(ImovelDTO::create).collect(Collectors.toList()));
	}

	@PostMapping()
	@ApiOperation("Salva um novo Locador")
	@ApiResponses({
        @ApiResponse(code = 201, message = "Locador salvo com sucesso"),
        @ApiResponse(code = 400, message = "Erro ao salvar o locador")
	})
	public ResponseEntity post(LocadorDTO dto) {
		try {
			Locador locador = converter(dto);
			Endereco endereco = enderecoService.salvar(locador.getEndereco());
			locador.setEndereco(endereco);
			locador = service.salvar(locador);
			return new ResponseEntity(locador, HttpStatus.CREATED);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	@ApiOperation("Altera dados do Locador")
	public ResponseEntity atualizar(@PathVariable("id") @ApiParam("Id do locador") Long id, LocadorDTO dto) {
		if (!service.getLocadorById(id).isPresent()) {
			return new ResponseEntity("Locador não encontrado", HttpStatus.NOT_FOUND);
		}
		try {
			Locador locador = converter(dto);
			Endereco endereco = locador.getEndereco();
			enderecoService.salvar(endereco);
			service.salvar(locador);
			return new ResponseEntity(locador, HttpStatus.CREATED);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("{id}")
	@ApiOperation("Excluir um locador")
	public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do locador") Long id) {
		Optional<Locador> locador = service.getLocadorById(id);
		if (!locador.isPresent()) {
			return new ResponseEntity("Locador não encontrado", HttpStatus.NOT_FOUND);
		}
		try {
			service.excluir(locador.get());
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public Locador converter(LocadorDTO dto) {
		ModelMapper modelMapper = new ModelMapper();

		Locador locador = modelMapper.map(dto, Locador.class);
		Endereco endereco = modelMapper.map(dto, Endereco.class);

		locador.setEndereco(endereco);

		return locador;
	}

}
