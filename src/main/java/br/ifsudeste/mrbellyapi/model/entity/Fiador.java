package br.ifsudeste.mrbellyapi.model.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fiador extends Pessoa {
	@OneToOne(cascade = CascadeType.ALL)
	private Endereco endereco;

	@OneToMany(mappedBy = "fiador")
	private List<Contrato> contratos;
}
