package it.dedagroup.venditabiglietti.principal.service;

import it.dedagroup.venditabiglietti.principal.dto.request.AggiungiSettoreDtoRequest;
import it.dedagroup.venditabiglietti.principal.dto.response.SettoreMicroDTO;
import it.dedagroup.venditabiglietti.principal.model.Settore;

import java.util.List;

public interface SettoreServiceDef {
	
	void aggiungiSettore(AggiungiSettoreDtoRequest dto);
	List<SettoreMicroDTO> findAllByIdLuogo(long idLuogo);
	List<SettoreMicroDTO> findAllByListIdsLuogo(List<Long> ids);

	SettoreMicroDTO findById(long idSettore);
}
