package br.ifsudeste.mrbellyapi.api.controller;

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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/contratos")
@RequiredArgsConstructor
public class ContratoController {

	private final ContratoService service;
	private final ImovelService imovelService;
	private final LocatarioService locatarioService;
	private final FiadorService fiadorService;

	@GetMapping()
	public ResponseEntity get() {
		List<Contrato> contratos = service.getContratos();
		return ResponseEntity.ok(contratos.stream().map(ContratoDTO::create).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public ResponseEntity get(@PathVariable("id") Long id) {
		Optional<Contrato> contrato = service.getContratoById(id);
		if (!contrato.isPresent()) {
			return new ResponseEntity("Contrato não encontrado", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(contrato.map(ContratoDTO::create));
	}

	@PostMapping()
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
	public ResponseEntity excluir(@PathVariable("id") Long id) {
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
