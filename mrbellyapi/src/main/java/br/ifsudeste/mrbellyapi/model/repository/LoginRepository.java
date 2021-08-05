package br.ifsudeste.mrbellyapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsudeste.mrbellyapi.model.entity.Login;

public interface LoginRepository extends JpaRepository<Login, Long>{

}
