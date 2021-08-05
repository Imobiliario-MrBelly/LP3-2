package br.ifsudeste.mrbellyapi.model.entity;

import javax.persistence.*;

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
    @ManyToOne
    private Locador locador;
    @OneToOne(cascade = CascadeType.ALL)
    private Endereco endereco;
}
