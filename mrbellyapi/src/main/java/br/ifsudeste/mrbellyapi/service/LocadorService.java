package br.ifsudeste.mrbellyapi.service;

import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Locador;
import br.ifsudeste.mrbellyapi.model.repository.LocadorRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LocadorService {
	private LocadorRepository repository;

	public LocadorService(LocadorRepository repository) {
		this.repository = repository;
	}

	public List<Locador> getLocadores() {

		return repository.findAll();
	}

	public Optional<Locador> getLocadorById(Long id) {
		return repository.findById(id);
	}

	@Transactional
	public Locador salvar(Locador locador) {
		validar(locador);
		return repository.save(locador);
	}
	
	@Transactional
    public void excluir(Locador locador) {
        Objects.requireNonNull(locador.getId());
        repository.delete(locador);
    }

	private void validar(Locador locador) {
		if (locador.getNome()==null||locador.getNome().trim().equals("")){
			throw new RegraDeNegocioException("Nome inválido");
		}

		if (locador.getCpf()==null||locador.getCpf().trim().equals("")){
			throw new RegraDeNegocioException("CPF inválido");
		}

		if (locador.getRg()==null||locador.getRg().trim().equals("")){
			throw new RegraDeNegocioException("RG inválido");
		}
		if (locador.getSexo()==null||locador.getSexo().trim().equals("")){
			throw new RegraDeNegocioException("Sexo inválido");
		}
		if (locador.getDataNascimento()==null){
			throw new RegraDeNegocioException("Data inválida");
		}
		if (locador.getEndereco()==null){
			throw new RegraDeNegocioException("Endereco inválido");
		}

	}


}
