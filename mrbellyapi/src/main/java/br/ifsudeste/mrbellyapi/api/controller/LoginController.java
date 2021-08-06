package br.ifsudeste.mrbellyapi.api.controller;

import br.ifsudeste.mrbellyapi.api.dto.LoginDTO;
import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Login;
import br.ifsudeste.mrbellyapi.service.LoginService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/logins")
@RequiredArgsConstructor
public class LoginController {

	private final LoginService service;
	private final PasswordEncoder passwordEncoder;

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



	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Login salvar(@RequestBody Login login ){
		String senhaCriptografada = passwordEncoder.encode(login.getSenha());
		login.setSenha(senhaCriptografada);
		return service.salvar(login);
	}
}
