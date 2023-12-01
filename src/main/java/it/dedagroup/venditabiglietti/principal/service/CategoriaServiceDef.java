package it.dedagroup.venditabiglietti.principal.service;

import it.dedagroup.venditabiglietti.principal.dto.request.AggiungiCategoriaDtoRequest;
import it.dedagroup.venditabiglietti.principal.dto.request.CategoriaCriteriaDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.EventiFiltratiDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.CategoriaMicroDTO;

import java.util.List;

public interface CategoriaServiceDef {
	
	   void aggiungiCategoria(AggiungiCategoriaDtoRequest request); 
	   CategoriaMicroDTO findById(long id);
	   List<CategoriaMicroDTO> criteriaCategorieFiltrate(CategoriaCriteriaDTORequest request);
}
