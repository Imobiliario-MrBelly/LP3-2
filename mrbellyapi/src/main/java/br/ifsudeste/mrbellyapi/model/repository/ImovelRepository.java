package br.ifsudeste.mrbellyapi.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsudeste.mrbellyapi.model.entity.Imovel;
import br.ifsudeste.mrbellyapi.model.entity.Locador;

public interface ImovelRepository extends JpaRepository<Imovel, Long> {

	List<Imovel> findByLocador(Optional<Locador> locador);
}
