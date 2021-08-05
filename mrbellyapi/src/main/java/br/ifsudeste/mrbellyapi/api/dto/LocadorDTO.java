package br.ifsudeste.mrbellyapi.api.dto;

import br.ifsudeste.mrbellyapi.model.entity.Locador;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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

	private String email;
	private String senha;

	public static LocadorDTO create(Locador locador) {
		ModelMapper modelMapper = new ModelMapper();
		LocadorDTO dto = modelMapper.map(locador, LocadorDTO.class);

		dto.rua = locador.getEndereco().getRua();
		dto.numero = locador.getEndereco().getNumero();
		dto.bairro = locador.getEndereco().getBairro();
		dto.cep = locador.getEndereco().getCep();
		dto.cidade = locador.getEndereco().getCidade();
		dto.uf = locador.getEndereco().getUf();

		dto.email = locador.getLogin().getEmail();
		dto.senha = locador.getLogin().getSenha();

		return dto;
	}
}
