package br.ifsudeste.mrbellyapi.service;

import br.ifsudeste.mrbellyapi.api.exception.SenhaInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ifsudeste.mrbellyapi.model.entity.Usuario;
import br.ifsudeste.mrbellyapi.model.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UsuarioRepository repository;

	@Transactional
	public Usuario salvar(Usuario usuario) {
		return repository.save(usuario);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = repository.findByLogin(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

		String[] roles = usuario.isAdmin() ? new String[] { "ADMIN", "USER" } : new String[] { "USER" };

		return User.builder().username(usuario.getLogin()).password(usuario.getSenha()).roles(roles).build();
	}
	public UserDetails autenticar(Usuario usuario){
		UserDetails user = loadUserByUsername(usuario.getLogin());
		boolean senhasBatem = encoder.matches(usuario.getSenha(), user.getPassword());

		if (senhasBatem){
			return user;
		}
		throw new SenhaInvalidaException();
	}

}
