package br.ifsudeste.mrbellyapi.model.repository;

import br.ifsudeste.mrbellyapi.model.entity.Imovel;
import br.ifsudeste.mrbellyapi.model.entity.Locador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImovelRepository extends JpaRepository<Imovel, Long>{

    List<Imovel> findByLocador(Optional<Locador> locador);
}
