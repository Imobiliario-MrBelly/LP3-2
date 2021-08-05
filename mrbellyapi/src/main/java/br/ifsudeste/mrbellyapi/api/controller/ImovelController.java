package br.ifsudeste.mrbellyapi.api.controller;

import br.ifsudeste.mrbellyapi.api.dto.ImovelDTO;
import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Endereco;
import br.ifsudeste.mrbellyapi.model.entity.Fiador;
import br.ifsudeste.mrbellyapi.model.entity.Imovel;
import br.ifsudeste.mrbellyapi.model.entity.Locador;
import br.ifsudeste.mrbellyapi.service.EnderecoService;
import br.ifsudeste.mrbellyapi.service.ImovelService;
import br.ifsudeste.mrbellyapi.service.LocadorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/imoveis")
@RequiredArgsConstructor
public class ImovelController {
	private final ImovelService service;
	private final EnderecoService enderecoService;
	private final LocadorService locadorService;

	@GetMapping()
	public ResponseEntity get() {
		List<Imovel> imoveis = service.getImoveis();
		return ResponseEntity.ok(imoveis.stream().map(ImovelDTO::create));
	}

	@GetMapping("/{id}")
	public ResponseEntity get(@PathVariable("id") Long id) {
		Optional<Imovel> imovel = service.getImovelById(id);
		if (!imovel.isPresent()) {
			return new ResponseEntity("Imóvel não encontrado", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(imovel.map(ImovelDTO::create));
	}

	@PostMapping()
	public ResponseEntity post(ImovelDTO dto) {
		try {
			Imovel imovel = converter(dto);
			Endereco endereco = enderecoService.salvar(imovel.getEndereco());
			imovel.setEndereco(endereco);
			imovel = service.salvar(imovel);
			return new ResponseEntity(imovel, HttpStatus.CREATED);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity ataulizar(@PathVariable("id") Long id, ImovelDTO dto) {
		if (!service.getImovelById(id).isPresent()) {
			return new ResponseEntity("Imóvel não encontrado", HttpStatus.NOT_FOUND);
		}

		try {
			Imovel imovel = converter(dto);
			imovel.setId(id);
			service.salvar(imovel);
			return ResponseEntity.ok(imovel);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity excluir(@PathVariable("id") Long id) {
		Optional<Imovel> imovel = service.getImovelById(id);
		if (!imovel.isPresent()) {
			return new ResponseEntity("Imóvel não encontrado", HttpStatus.NOT_FOUND);
		}
		try {
			service.excluir(imovel.get());
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		} catch (RegraDeNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	public Imovel converter(ImovelDTO dto) {

		ModelMapper modelMapper = new ModelMapper();
		Imovel imovel = modelMapper.map(dto, Imovel.class);

		Endereco endereco = modelMapper.map(dto, Endereco.class);
		imovel.setEndereco(endereco);

		if (dto.getIdLocador() != null) {
			Optional<Locador> locador = locadorService.getLocadorById(dto.getIdLocador());
			if (!locador.isPresent()) {
				imovel.setLocador(null);
			} else {
				imovel.setLocador(locador.get());
			}
		}
		return imovel;
	}
}
