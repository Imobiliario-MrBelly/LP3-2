package br.ifsudeste.mrbellyapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsudeste.mrbellyapi.model.entity.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long>{
}
