package br.ifsudeste.mrbellyapi.api.controller;

import br.ifsudeste.mrbellyapi.api.dto.LoginDTO;
import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Login;
import br.ifsudeste.mrbellyapi.service.LoginService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/logins")
@RequiredArgsConstructor
public class LoginController {

	private final LoginService service;

	@GetMapping()
	public ResponseEntity get() {
		List<Login> logins = service.getLogins();
		return ResponseEntity.ok(logins.stream().map(LoginDTO::create).collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	public ResponseEntity get(@PathVariable("id") Long id) {
		Optional<Login> login = service.getLoginById(id);
		if (!login.isPresent()) {
			return new ResponseEntity("Login não encontrado", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(login.map(LoginDTO::create));
	}

	public Login converter(LoginDTO dto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(dto, Login.class);
	}
}
