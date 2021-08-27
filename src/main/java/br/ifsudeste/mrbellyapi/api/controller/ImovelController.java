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

import br.ifsudeste.mrbellyapi.api.dto.ImovelDTO;
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
@RequestMapping("/api/v1/imoveis")
@RequiredArgsConstructor
@Api("API de Imóveis")
public class ImovelController {
	private final ImovelService service;
	private final EnderecoService enderecoService;
	private final LocadorService locadorService;

	@GetMapping()
	@ApiOperation("Obter todos os imóveis")
	public ResponseEntity get() {
		List<Imovel> imoveis = service.getImoveis();
		return ResponseEntity.ok(imoveis.stream().map(ImovelDTO::create));
	}

	@GetMapping("/{id}")
	@ApiOperation("Obter detalhes de um imóvel")
	@ApiResponses({ 
		@ApiResponse(code = 200, message = "Imóvel encontrado"),
		@ApiResponse(code = 404, message = "Imóvel não encontrado")
	})
	public ResponseEntity get(@PathVariable("id") @ApiParam("Id do imóvel") Long id) {
		Optional<Imovel> imovel = service.getImovelById(id);
		if (!imovel.isPresent()) {
			return new ResponseEntity("Imóvel não encontrado", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(imovel.map(ImovelDTO::create));
	}

	@PostMapping()
	@ApiOperation("Salva um novo imóvel")
	@ApiResponses({
        @ApiResponse(code = 201, message = "Imóvel salvo com sucesso"),
        @ApiResponse(code = 400, message = "Erro ao salvar o imóvel")
	})
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
	@ApiOperation("Altera dados de imóveis")
	public ResponseEntity ataulizar(@PathVariable("id") @ApiParam("Id do imóvel") Long id, ImovelDTO dto) {
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
	@ApiOperation("Excluir um imóvel")
	public ResponseEntity excluir(@PathVariable("id") @ApiParam("Id do imóvel") Long id) {
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
