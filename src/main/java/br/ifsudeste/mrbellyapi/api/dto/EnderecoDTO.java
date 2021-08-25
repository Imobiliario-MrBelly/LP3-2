package br.ifsudeste.mrbellyapi.api.dto;

import org.modelmapper.ModelMapper;

import br.ifsudeste.mrbellyapi.model.entity.Endereco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO {
	private Long id;

	private String rua;
	private String numero;
	private String bairro;
	private String cep;
	private String cidade;
	private String uf;

	public static EnderecoDTO create(Endereco endereco) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(endereco, EnderecoDTO.class);
	}
}
