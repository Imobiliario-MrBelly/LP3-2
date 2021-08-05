package br.ifsudeste.mrbellyapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsudeste.mrbellyapi.model.entity.Contrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long>{

}
