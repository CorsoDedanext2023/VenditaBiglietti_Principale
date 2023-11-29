package it.dedagroup.venditabiglietti.principal.service;

import it.dedagroup.venditabiglietti.principal.dto.response.LuogoMicroDTO;
import it.dedagroup.venditabiglietti.principal.model.Luogo;

import java.util.List;
import java.util.Map;

public interface LuogoServiceDef {
	
	List<LuogoMicroDTO> findByCriteriaQuery(Map<String, String> parametriLuogo);
    
	List<LuogoMicroDTO> filtraLuoghiMap(Map<String, String> mapLuogo);
	public LuogoMicroDTO save(Luogo luogo);
    public LuogoMicroDTO modify(Luogo luogo);
    public void deleteLuogoById(long id);
    public List<LuogoMicroDTO> findAll();
    public List<LuogoMicroDTO> findAllByIds(List<Long> ids);
    public LuogoMicroDTO findLuogoById(long id);
    public List<LuogoMicroDTO> findAllLuogoByRiga1AndComune(String riga1, String comune);
    public List<LuogoMicroDTO> findAllLuogoByRiga1(String riga1);
    public List<LuogoMicroDTO> findAllByRiga1AndRiga2AndComune(String riga1, String riga2, String comune);
    public List<LuogoMicroDTO> findAllByRiga1AndRiga2(String riga1, String riga2);
    public List<LuogoMicroDTO> findAllLuogoByCap(String cap);
    public List<LuogoMicroDTO> findAllLuogoByComune(String comune);
    public List<LuogoMicroDTO> findAllLuogoByProvincia(String provincia);
    public List<LuogoMicroDTO> findAllLuogoByNazionalita(String nazionalita);
    public List<LuogoMicroDTO> findAllLuogoByNazionalitaAndComune(String nazionalita, String comune);
}
