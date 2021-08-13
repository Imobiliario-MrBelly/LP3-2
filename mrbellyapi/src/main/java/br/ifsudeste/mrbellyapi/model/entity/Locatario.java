package br.ifsudeste.mrbellyapi.model.entity;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Locatario extends Pessoa {

	private String cargoTrabalho;
	private String empresaTrabalho;
	private double renda;
	private int qtdResidentes;
}
