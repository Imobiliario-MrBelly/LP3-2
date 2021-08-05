package br.ifsudeste.mrbellyapi.api.dto;

import br.ifsudeste.mrbellyapi.model.entity.Imovel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImovelDTO {
	
	private Long id;
	private double area;
	private String descricao;
	private double condominio;
	private double iptu;
	private int garagem;
	private Long idLocador;
	
	private String rua;
	private String numero;
	private String bairro;
	private String cep;
	private String cidade;
	private String uf;

	public static ImovelDTO create(Imovel imovel) {
		ModelMapper modelMapper = new ModelMapper();
		ImovelDTO dto = modelMapper.map(imovel, ImovelDTO.class);
		
		assert dto.getIdLocador().equals(imovel.getLocador().getId());
		
		dto.rua = imovel.getEndereco().getRua();
		dto.numero = imovel.getEndereco().getNumero();
		dto.bairro = imovel.getEndereco().getBairro();
		dto.cep = imovel.getEndereco().getCep();
		dto.cidade = imovel.getEndereco().getCidade();
		dto.uf = imovel.getEndereco().getUf();
		
		return dto;
	}
}
