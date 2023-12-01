package it.dedagroup.venditabiglietti.principal.serviceimpl;

import it.dedagroup.venditabiglietti.principal.dto.request.CategoriaCriteriaDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.EventiFiltratiDTORequest;
import org.springframework.stereotype.Service;

import it.dedagroup.venditabiglietti.principal.dto.request.AggiungiCategoriaDtoRequest;
import it.dedagroup.venditabiglietti.principal.dto.response.CategoriaMicroDTO;
import it.dedagroup.venditabiglietti.principal.service.CategoriaServiceDef;
import it.dedagroup.venditabiglietti.principal.service.GeneralCallService;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaServiceDef, GeneralCallService {
	
	
	
	@Override
	public void aggiungiCategoria(AggiungiCategoriaDtoRequest request) {
		String path = "http://localhost:8082/categoria/aggiungicategoria";
		callPost(path, request , Void.class);		
	}

	@Override
	public CategoriaMicroDTO findById(long id) {
		String path = "http://localhost:8082/categoria/trova/" + id;
		return callGet(path, id, CategoriaMicroDTO.class);
	}

	@Override
	public List<CategoriaMicroDTO> criteriaCategorieFiltrate(CategoriaCriteriaDTORequest request) {
		String path = "http://localhost:8082/categoria/filtraCategorie";
		return callPostForList(path, request, CategoriaMicroDTO[].class);
	}


}
