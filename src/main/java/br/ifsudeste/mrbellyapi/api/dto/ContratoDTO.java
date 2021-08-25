package br.ifsudeste.mrbellyapi.api.dto;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.ifsudeste.mrbellyapi.model.entity.Contrato;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoDTO {

	private Long id;
	private Long idImovel;
	private Long idLocatario;
	private Long idFiador;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataInicio;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataFim;

	private double valor;

	public static ContratoDTO create(Contrato contrato) {
		ModelMapper modelMapper = new ModelMapper();
		ContratoDTO dto = modelMapper.map(contrato, ContratoDTO.class);

		assert dto.getIdImovel().equals(contrato.getImovel().getId());
		assert dto.getIdLocatario().equals(contrato.getLocatario().getId());
		assert dto.getIdFiador().equals(contrato.getFiador().getId());

		return dto;
	}
}
