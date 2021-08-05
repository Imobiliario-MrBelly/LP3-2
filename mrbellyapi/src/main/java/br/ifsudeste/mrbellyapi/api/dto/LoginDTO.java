package br.ifsudeste.mrbellyapi.api.dto;

import br.ifsudeste.mrbellyapi.model.entity.Login;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

	private Long id;
	private String email;
	private String senha;

	public static LoginDTO create(Login login) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(login, LoginDTO.class);
	}
}
