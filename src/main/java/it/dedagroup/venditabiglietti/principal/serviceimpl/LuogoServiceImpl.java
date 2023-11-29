package it.dedagroup.venditabiglietti.principal.serviceimpl;
import it.dedagroup.venditabiglietti.principal.dto.response.LuogoMicroDTO;
import it.dedagroup.venditabiglietti.principal.model.Luogo;
import it.dedagroup.venditabiglietti.principal.service.GeneralCallService;
import it.dedagroup.venditabiglietti.principal.service.LuogoServiceDef;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class LuogoServiceImpl implements LuogoServiceDef, GeneralCallService {

    String pathLuogo=("http://localhost:8088/luogo/");
    @Override
    public List<LuogoMicroDTO> findByCriteriaQuery(Map<String, String> parametriLuogo){
        return callPostForList(pathLuogo+"find/allByIds", parametriLuogo, LuogoMicroDTO[].class);
    }
    @Transactional(rollbackOn = ResponseStatusException.class)
    @Override
    public LuogoMicroDTO save(Luogo luogo) {
        return callPost(pathLuogo+"save", luogo, LuogoMicroDTO.class);
    }
    @Transactional(rollbackOn = ResponseStatusException.class)
    @Override
    public LuogoMicroDTO modify(Luogo luogo) {
        return callPost(pathLuogo+"modify",luogo, LuogoMicroDTO.class);
    }

    @Transactional(rollbackOn = ResponseStatusException.class)
    @Override
    public void deleteLuogoById(long id) {
        callPost(pathLuogo+"delete/"+id,id,String.class);
    }

    @Override
    public List<LuogoMicroDTO> findAll() {
        return callGetForList(pathLuogo+"find/all",null,LuogoMicroDTO[].class);
    }

    @Override
    public List<LuogoMicroDTO> findAllByIds(List<Long> ids) {
        return callPostForList(pathLuogo+"find/allByIds", ids, LuogoMicroDTO[].class);
    }

    @Override
    public LuogoMicroDTO findLuogoById(long id) {
        return callGet(pathLuogo+"find/id/"+id,null,LuogoMicroDTO.class);
    }

    @Override
    public List<LuogoMicroDTO> findAllLuogoByRiga1AndComune(String riga1, String comune) {
        return callGetForList(pathLuogo+"find/all/riga1&comune/"+riga1+"/"+comune,null,LuogoMicroDTO[].class);
    }

    @Override
    public List<LuogoMicroDTO> findAllLuogoByRiga1(String riga1) {
        return callGetForList(pathLuogo+"find/all/riga1/"+riga1,null,LuogoMicroDTO[].class);
    }

    @Override
    public List<LuogoMicroDTO> findAllByRiga1AndRiga2AndComune(String riga1, String riga2, String comune) {
        return callGetForList(pathLuogo+"find/all/riga1&riga2&comune/"+riga1+"/"+riga2+"/"+comune,null,LuogoMicroDTO[].class);
    }

    @Override
    public List<LuogoMicroDTO> findAllByRiga1AndRiga2(String riga1, String riga2) {
        return callGetForList(pathLuogo+"find/all/riga1&riga2/"+riga1+"/"+riga2,null,LuogoMicroDTO[].class);
    }

    @Override
    public List<LuogoMicroDTO> findAllLuogoByCap(String cap) {
        return callGetForList(pathLuogo+"find/all/cap/"+cap,null,LuogoMicroDTO[].class);
    }

    @Override
    public List<LuogoMicroDTO> findAllLuogoByComune(String comune) {
        return callGetForList(pathLuogo+"find/all/comune/"+comune,null,LuogoMicroDTO[].class);
    }

    @Override
    public List<LuogoMicroDTO> findAllLuogoByProvincia(String provincia) {
        return callGetForList(pathLuogo+"find/all/provincia/"+provincia,null,LuogoMicroDTO[].class);
    }

    @Override
    public List<LuogoMicroDTO> findAllLuogoByNazionalita(String nazionalita) {
        return callGetForList(pathLuogo+"find/all/nazionalita/"+nazionalita,null,LuogoMicroDTO[].class);
    }

    @Override
    public List<LuogoMicroDTO> findAllLuogoByNazionalitaAndComune(String nazionalita, String comune) {
        return callGetForList(pathLuogo+"find/all/nazionalita&comune/"+nazionalita+"/"+comune,null,LuogoMicroDTO[].class);
    }
	@Override
	public List<LuogoMicroDTO> filtraLuoghiMap(Map<String, String> mapLuogo) {
		return callPostForList(pathLuogo+"filtroLuogoMap", mapLuogo, LuogoMicroDTO[].class);
	}


}
