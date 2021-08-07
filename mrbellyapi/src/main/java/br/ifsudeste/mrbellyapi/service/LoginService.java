package br.ifsudeste.mrbellyapi.service;

import br.ifsudeste.mrbellyapi.model.entity.Login;
import br.ifsudeste.mrbellyapi.model.repository.LoginRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LoginService implements UserDetailsService {
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private LoginRepository repository;

	@Transactional
	public Login salvar(Login login) {
		return repository.save(login);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Login login = repository.findByLogin(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

		String[] roles = login.isAdmin() ? new String[] { "ADMIN", "USER" } : new String[] { "USER" };

		return User.builder().username(login.getEmail()).password(login.getSenha()).roles(roles).build();
	}
}
