package br.ifsudeste.mrbellyapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fiador extends Pessoa{
    @OneToOne(cascade = CascadeType.ALL)
    private Endereco endereco;
}
