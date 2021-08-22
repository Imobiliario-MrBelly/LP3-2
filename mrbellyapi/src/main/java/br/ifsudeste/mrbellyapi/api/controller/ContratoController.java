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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsudeste.mrbellyapi.api.dto.ContratoDTO;
import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Contrato;
import br.ifsudeste.mrbellyapi.model.entity.Fiador;
import br.ifsudeste.mrbellyapi.model.entity.Imovel;
import br.ifsudeste.mrbellyapi.model.entity.Locatario;
import br.ifsudeste.mrbellyapi.service.ContratoService;
import br.ifsudeste.mrbellyapi.service.FiadorService;
import br.ifsudeste.mrbellyapi.service.ImovelService;
import br.ifsudeste.mrbellyapi.service.LocatarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/contratos")
@RequiredArgsConstructor
@Api("API de Contratos")
public class ContratoController {

	private final ContratoService service;
	private final ImovelService imovelService;
	private final LocatarioService locatarioService;
	private final FiadorService fiadorService;

	@GetMapping()
	@ApiOperation("Obter todos os contratos")
	public ResponseEntity get() {
		List<Contrato> contratos = service.getContratos();
		return ResponseEntity.ok(contratos.stream().map(ContratoDTO::create).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	@ApiOperation("Obter detalhes de um contrato")
	@ApiResponses({ 
		@ApiResponse(code = 200, message = "Contrato encontrado"),
		@ApiResponse(code = 404, message = "Contrato não encontrado")
	})
	public ResponseEntity get(@PathVariable("id") @ApiParam("Id do contrato") Long id) {
		Optional<Contrato> contrato = service.getContratoById(id);
		if (!contrato.isPresent()) {
			return new ResponseEntity("Contrato não encontrado", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(contrato.map(ContratoDTO::create));
	}

	@PostMapping()
	@ApiOperation("Salva um novo contrato")
	@ApiResponses({
        @ApiResponse(code = 201, message = "Contrato salvo com sucesso"),
        @ApiResponse(code = 400, message = "Erro ao salvar o contrato")
	})
	public ResponseEntity post(ContratoDTO dto) {
		try {
			Contrato contrato = converter(dto);
			contrato = service.salvar(contrato);
			return new ResponseEntity(contrato, HttpStatus.CREATED);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("{id}")
	@ApiOperation("Excluir um contrato")
	public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do contrato") Long id) {
		
		Optional<Contrato> contrato = service.getContratoById(id);
		
		if (!contrato.isPresent()) {
			return new ResponseEntity("Contrato não encontrado", HttpStatus.NOT_FOUND);
		}
		try {
			service.excluir(contrato.get());
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public Contrato converter(ContratoDTO dto) {

		ModelMapper modelMapper = new ModelMapper();
		Contrato contrato = modelMapper.map(dto, Contrato.class);

		if (dto.getIdImovel() != null) {
			Optional<Imovel> imovel = imovelService.getImovelById(dto.getIdImovel());
			if (!imovel.isPresent()) {
				contrato.setImovel(null);
			} else {
				contrato.setImovel(imovel.get());
			}
		}

		if (dto.getIdLocatario() != null) {
			Optional<Locatario> locatario = locatarioService.getLocatarioById(dto.getIdLocatario());
			if (!locatario.isPresent()) {
				contrato.setLocatario(null);
			} else {
				contrato.setLocatario(locatario.get());
			}
		}

		if (dto.getIdFiador() != null) {
			Optional<Fiador> fiador = fiadorService.getFiadorById(dto.getIdFiador());
			if (!fiador.isPresent()) {
				contrato.setFiador(null);
			} else {
				contrato.setFiador(fiador.get());
			}
		}
		return contrato;
	}
}
