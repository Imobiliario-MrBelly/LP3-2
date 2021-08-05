package br.ifsudeste.mrbellyapi.service;

import br.ifsudeste.mrbellyapi.api.exception.RegraDeNegocioException;
import br.ifsudeste.mrbellyapi.model.entity.Locatario;
import br.ifsudeste.mrbellyapi.model.repository.LocatarioRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LocatarioService {
    private LocatarioRepository repository;

    public LocatarioService(LocatarioRepository repository){
        this.repository = repository;
    }

    public List<Locatario> getLocatarios(){
        return repository.findAll();
    }

    public Optional<Locatario> getLocatarioById(Long id){
       return repository.findById(id);
    }
    
    @Transactional
    public Locatario salvar(Locatario locatario) {
        validar(locatario);
        return repository.save(locatario);
    }
    
    @Transactional
    public void excluir(Locatario locatario) {
        Objects.requireNonNull(locatario.getId());
        repository.delete(locatario);
    }

    public void validar(Locatario locatario) {

    	if (locatario.getNome()==null||locatario.getNome().trim().equals("")){
            throw new RegraDeNegocioException("Nome inválido");
        }
        if (locatario.getCpf()==null||locatario.getCpf().trim().equals("")){
            throw new RegraDeNegocioException("CPF inválido");
        }
        if (locatario.getRg()==null||locatario.getRg().trim().equals("")){
            throw new RegraDeNegocioException("RG inválido");
        }
        if (locatario.getSexo()==null||locatario.getSexo().trim().equals("")){
            throw new RegraDeNegocioException("Sexo inválido");
        }
        if (locatario.getDataNascimento()==null){
            throw new RegraDeNegocioException("Data inválida");
        }
        if (locatario.getLogin() == null) {
            throw new RegraDeNegocioException("Login inválido");
        }
    }
}
