package br.ifsudeste.mrbellyapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsudeste.mrbellyapi.model.entity.Locatario;

public interface LocatarioRepository extends JpaRepository<Locatario, Long>{

}
