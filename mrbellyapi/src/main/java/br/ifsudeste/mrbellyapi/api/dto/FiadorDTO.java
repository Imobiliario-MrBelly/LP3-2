package br.ifsudeste.mrbellyapi.api.dto;

import br.ifsudeste.mrbellyapi.model.entity.Fiador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiadorDTO {

	private Long id;
	private String nome;
	private String sobrenome;
	private String rg;
	private String cpf;
	private String sexo;

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

	public static FiadorDTO create(Fiador fiador) {
		ModelMapper modelMapper = new ModelMapper();
		FiadorDTO dto = modelMapper.map(fiador, FiadorDTO.class);

		dto.rua = fiador.getEndereco().getRua();
		dto.numero = fiador.getEndereco().getNumero();
		dto.bairro = fiador.getEndereco().getBairro();
		dto.cep = fiador.getEndereco().getCep();
		dto.cidade = fiador.getEndereco().getCidade();
		dto.uf = fiador.getEndereco().getUf();

		return dto;
	}
}
