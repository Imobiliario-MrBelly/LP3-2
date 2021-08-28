package br.ifsudeste.mrbellyapi.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Contrato;
import br.ifsudeste.mrbellyapi.model.entity.Fiador;
import br.ifsudeste.mrbellyapi.model.repository.FiadorRepository;

@Service
public class FiadorService {
	private FiadorRepository repository;
	private final ContratoService contratoService;

	public FiadorService(FiadorRepository repository, ContratoService contratoService) {
		this.repository = repository;
		this.contratoService = contratoService;
	}

	public List<Fiador> getFiadores() {
		return repository.findAll();
	}

	public Optional<Fiador> getFiadorById(Long id) {
		return repository.findById(id);
	}

	@Transactional
	public Fiador salvar(Fiador fiador) {
		validar(fiador);
		return repository.save(fiador);
	}

	@Transactional
	public void excluir(Fiador fiador) {
		Objects.requireNonNull(fiador.getId());
		for (Contrato contrato : fiador.getContratos()) {            
            contrato.setFiador(null);
            contratoService.salvar(contrato);
        }
		repository.delete(fiador);
	}

	private void validar(Fiador fiador) {
		if (fiador.getNome() == null || fiador.getNome().trim().equals("")) {
			throw new RegraDeNegocioException("Nome não informado");
		}

		if (fiador.getCpf() == null || fiador.getCpf().trim().equals("")) {
			throw new RegraDeNegocioException("CPF não informado");
		}

		if (fiador.getRg() == null || fiador.getRg().trim().equals("")) {
			throw new RegraDeNegocioException("RG não informado");
		}
		if (fiador.getSexo() == null || fiador.getSexo().trim().equals("")) {
			throw new RegraDeNegocioException("Sexo inválido");
		}
		if (fiador.getDataNascimento() == null) {
			throw new RegraDeNegocioException("Data inválida");
		}
		if (fiador.getEndereco() == null) {
			throw new RegraDeNegocioException("Endereco inválido");
		}

	}

}
