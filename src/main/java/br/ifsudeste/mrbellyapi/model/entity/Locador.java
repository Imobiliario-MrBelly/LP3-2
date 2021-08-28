package br.ifsudeste.mrbellyapi.model.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Locador extends Pessoa {

	@OneToOne(cascade = CascadeType.ALL)
	private Endereco endereco;
	
	@JsonIgnore
	@OneToMany(mappedBy = "locador")
	@JoinTable(name = "imoveis_locador", joinColumns = @JoinColumn(name = "locador_id"), inverseJoinColumns = @JoinColumn(name = "imovel_id"))
	private List<Imovel> imoveis;
}
