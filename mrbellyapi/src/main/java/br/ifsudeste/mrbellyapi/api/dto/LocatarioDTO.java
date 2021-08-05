package br.ifsudeste.mrbellyapi.api.dto;

import br.ifsudeste.mrbellyapi.model.entity.Locatario;
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

	private String email;
	private String senha;

	public static LocatarioDTO create(Locatario locatario) {
		ModelMapper modelMapper = new ModelMapper();
		LocatarioDTO dto = modelMapper.map(locatario, LocatarioDTO.class);
		dto.email=locatario.getLogin().getEmail();
		dto.senha=locatario.getLogin().getSenha();
		return dto;
	}
}
