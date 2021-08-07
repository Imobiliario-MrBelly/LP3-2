package br.ifsudeste.mrbellyapi.api.dto;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.ifsudeste.mrbellyapi.model.entity.Locatario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocatarioDTO {

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

	private String cargoTrabalho;
	private String empresaTrabalho;
	private double renda;
	private int qtdResidentes;

	public static LocatarioDTO create(Locatario locatario) {
		ModelMapper modelMapper = new ModelMapper();
		LocatarioDTO dto = modelMapper.map(locatario, LocatarioDTO.class);
		return dto;
	}
}
