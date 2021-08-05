package br.ifsudeste.mrbellyapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Locador extends Pessoa {

	@OneToOne
	private Login login;

	@OneToOne(cascade = CascadeType.ALL)
	private Endereco endereco;

	@OneToMany
	@JoinTable(name = "imoveis_locador", joinColumns = @JoinColumn(name = "locador_id"), inverseJoinColumns = @JoinColumn(name = "imovel_id"))
	private List<Imovel> imoveis;
}
