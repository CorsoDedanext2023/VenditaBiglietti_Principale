package it.dedagroup.venditabiglietti.principal.serviceimpl;

import it.dedagroup.venditabiglietti.principal.dto.response.SettoreMicroDTO;
import org.springframework.stereotype.Service;

import it.dedagroup.venditabiglietti.principal.dto.request.AggiungiSettoreDtoRequest;
import it.dedagroup.venditabiglietti.principal.service.GeneralCallService;
import it.dedagroup.venditabiglietti.principal.service.SettoreServiceDef;

import java.util.List;

@Service
public class SettoreServiceImpl implements SettoreServiceDef, GeneralCallService {


	String path="http://localhost:8083/settore";
	@Override
	public void aggiungiSettore(AggiungiSettoreDtoRequest dto) {
		String mioPath=path+"/aggiungiSettore";
		callPost(mioPath, dto, Void.class);
	}

	@Override
	public List<SettoreMicroDTO> findAllByIdLuogo(long idLuogo) {
		String mioPath=path+"/findByIdLuogo?idLuogo="+idLuogo;
		return callGetForList(mioPath,idLuogo,SettoreMicroDTO[].class);
	}

	@Override
	public List<SettoreMicroDTO> findAllByListIdsLuogo(List<Long> ids) {
		return callPostForList(path+"/findAllByListIdLuogo",ids,SettoreMicroDTO[].class);
	}

	@Override
	public SettoreMicroDTO findById(long idSettore) {
		return callGet(path+"/findByIdEsistenti?id="+idSettore,idSettore,SettoreMicroDTO.class);
	}


}
