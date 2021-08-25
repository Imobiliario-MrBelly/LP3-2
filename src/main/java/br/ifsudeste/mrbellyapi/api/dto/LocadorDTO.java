package br.ifsudeste.mrbellyapi.api.dto;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.ifsudeste.mrbellyapi.model.entity.Locador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocadorDTO {

	private Long id;
	private String nome;
	private String sobrenome;
	private String rg;
	private String cpf;
	private char sexo;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataNascimento;
	private String telefone;

	private String rua;
	private String numero;
	private String bairro;
	private String cep;
	private String cidade;
	private String uf;

	public static LocadorDTO create(Locador locador) {
		ModelMapper modelMapper = new ModelMapper();
		LocadorDTO dto = modelMapper.map(locador, LocadorDTO.class);

		dto.rua = locador.getEndereco().getRua();
		dto.numero = locador.getEndereco().getNumero();
		dto.bairro = locador.getEndereco().getBairro();
		dto.cep = locador.getEndereco().getCep();
		dto.cidade = locador.getEndereco().getCidade();
		dto.uf = locador.getEndereco().getUf();

		return dto;
	}
}
