package br.ifsudeste.mrbellyapi.service;

import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Contrato;
import br.ifsudeste.mrbellyapi.model.repository.ContratoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ContratoService {
	private ContratoRepository repository;

	public ContratoService(ContratoRepository repository) {
		this.repository = repository;
	}

	public List<Contrato> getContratos() {
		return repository.findAll();
	}

	public Optional<Contrato> getContratoById(Long id) {
		return repository.findById(id);
	}

	@Transactional
	public Contrato salvar(Contrato contrato) {
		validar(contrato);
		return repository.save(contrato);
	}

	@Transactional
	public void excluir(Contrato contrato) {
		Objects.requireNonNull(contrato.getId());
		repository.delete(contrato);
	}

	public void validar(Contrato contrato) {

		if (contrato.getLocatario() == null) {
			throw new RegraDeNegocioException("Locatário não inserido");
		}

		if (contrato.getImovel() == null) {
			throw new RegraDeNegocioException("Imovél não inserido");
		}

		if (contrato.getValor() == 0) {
			throw new RegraDeNegocioException("Valor não inserido");
		}
		
		if (contrato.getDataInicio() != null && contrato.getDataFim() != null) {
			
			if (contrato.getDataFim().compareTo(contrato.getDataInicio()) <= 0) {
				throw new RegraDeNegocioException("Data inválida");
			}
		} else {
			throw new RegraDeNegocioException("Data não inserida");
		}

	}
}
