package br.ifsudeste.mrbellyapi.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Imovel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private double area;
	private String descricao;
	private double condominio;
	private double iptu;
	private int garagem;
	@JsonIgnore
	@ManyToOne
	@JoinTable(name = "imoveis_locador", joinColumns = @JoinColumn(name = "imovel_id"), inverseJoinColumns = @JoinColumn(name = "locador_id"))
	private Locador locador;
	@OneToOne(cascade = CascadeType.ALL)
	private Endereco endereco;
}
