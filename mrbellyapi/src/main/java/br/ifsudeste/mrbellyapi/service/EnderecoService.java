package br.ifsudeste.mrbellyapi.service;

import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Endereco;
import br.ifsudeste.mrbellyapi.model.repository.EnderecoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EnderecoService {
	private EnderecoRepository repository;

	public EnderecoService(EnderecoRepository repository) {
		this.repository = repository;
	}

	public List<Endereco> getEnderecos() {
		return repository.findAll();
	}

	public Optional<Endereco> getEnderecoById(Long id) {
		return repository.findById(id);
	}

	@Transactional
	public Endereco salvar(Endereco endereco) {
		validar(endereco);
		return repository.save(endereco);
	}

	@Transactional
    public void excluir(Endereco endereco) {
        Objects.requireNonNull(endereco.getId());
        repository.delete(endereco);
    }
	
	public void validar(Endereco endereco) {

		if (endereco.getRua() == null || endereco.getRua().trim().equals("")) {
			throw new RegraDeNegocioException("Rua não informado");
		}
		
		if (endereco.getNumero() == null || endereco.getNumero().trim().equals("")) {
			throw new RegraDeNegocioException("Número não informado");
		}
		
		if (endereco.getCep() == null || endereco.getCep().trim().equals("")) {
			throw new RegraDeNegocioException("CEP não informado");
		}
		
		if (endereco.getCidade() == null || endereco.getCidade().trim().equals("")) {
			throw new RegraDeNegocioException("Cidade não informado");
		}
		
		if (endereco.getUf() == null || endereco.getUf().trim().equals("")) {
			throw new RegraDeNegocioException("Unidade federativa (UF) não informado");
		}
		if (endereco.getBairro()==null|| endereco.getBairro().trim().equals("")){
			throw new RegraDeNegocioException("Bairro não informado");
		}
	}
}
