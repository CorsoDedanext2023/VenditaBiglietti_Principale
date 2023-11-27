package it.dedagroup.venditabiglietti.principal.serviceimpl;

import it.dedagroup.venditabiglietti.principal.dto.request.AddBigliettoDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.ModifyBigliettoDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.BigliettoMicroDTO;
import it.dedagroup.venditabiglietti.principal.service.BigliettoServiceDef;
import it.dedagroup.venditabiglietti.principal.service.GeneralCallService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static it.dedagroup.venditabiglietti.principal.util.BigliettoUtilPath.*;

@Service
public class BigliettoServiceImpl implements BigliettoServiceDef, GeneralCallService {

    @Override
    public BigliettoMicroDTO saveBiglietto(AddBigliettoDTORequest biglietto) {
        return callPost(INSERT_BIGLIETTO_PATH,biglietto,BigliettoMicroDTO.class);
    }

    @Override
    public BigliettoMicroDTO modifyBiglietto(ModifyBigliettoDTORequest biglietto) {
        return callPost(MODIFY_BIGLIETTO_PATH,biglietto,BigliettoMicroDTO.class);
    }

    @Override
    public String deleteByBiglietto(long id_biglietto) {
        return callPost(DELETE_BIGLIETTO_PATH+"/"+id_biglietto,null,String.class);
    }

    @Override
    public BigliettoMicroDTO findById(long id_biglietto) {
        return callPost(FIND_BY_ID_PATH+"/"+id_biglietto,null,BigliettoMicroDTO.class);
    }

    @Override
    public List<BigliettoMicroDTO> findAll() {
        return callGetForList(FIND_ALL_PATH,null,BigliettoMicroDTO[].class);
    }

    @Override
    public BigliettoMicroDTO findBySeriale(String seriale) {
        return callPost(FIND_BY_SERIALE_PATH+"/"+seriale,null,BigliettoMicroDTO.class);
    }

    @Override
    public List<BigliettoMicroDTO> findAllByPrezzoIsGreaterThanEqual(double prezzo) {
        return callGetForList(FIND_ALL_BY_PREZZO_IS_GREATER_THAN_EQUAL_PATH+"/"+prezzo,null,BigliettoMicroDTO[].class);
    }

    @Override
    public List<BigliettoMicroDTO> findAllByPrezzoIsLessThanEqual(double prezzo) {
        return callGetForList(FIND_ALL_BY_PREZZO_IS_LESS_THAN_EQUAL_PATH+prezzo,null,BigliettoMicroDTO[].class);
    }

    @Override
    public List<BigliettoMicroDTO> findAllByIdUtente(long id_utente) {
        return callPostForList(FIND_ALL_BY_ID_UTENTE_PATH+"/"+id_utente,null,BigliettoMicroDTO[].class);
    }

    @Override
    public List<BigliettoMicroDTO> findAllByDataAcquisto(LocalDate dataAcquisto) {
        return callGetForList(FIND_ALL_BY_DATA_ACQUISTO+"/"+dataAcquisto,null,BigliettoMicroDTO[].class);
    }

    @Override
    public List<BigliettoMicroDTO> findAllByIdPrezzoSettoreEventoOrderByPrezzoAsc(long id_prezzoSettoreEvento) {
        return callPostForList(BIGLIETTO_PATH + "/find/all/prezzo-settore-evento/id" + id_prezzoSettoreEvento,  null, BigliettoMicroDTO[].class);
    }

    @Override
    public int countByIdPrezzoSettoreEventoAndDataAcquistoIsNotNull(long id_prezzoSettoreEvento) {
        return callPost(BIGLIETTO_PATH+"/count/data-acquisto/not-null/prezzo-settore-evento/id/"+id_prezzoSettoreEvento,null,Integer.class);
    }

    @Override
    public List<Double> findDistinctPrezzoBigliettoByIdPrezzoSettoreEvento(long id_prezzoSettoreEvento) {
        return callPostForList(FIND_DISTINCT_PREZZO_BIGLIETTO_PATH+"/"+id_prezzoSettoreEvento,null,Double[].class);
    }

    @Override
    public List<BigliettoMicroDTO> findAllByIdPrezzoSettoreEventoIn(List<Long> idsPrezzoSettoreEvento) {
        return callPostForList(FIND_ALL_BY_ID_PREZZO_SETTORE_EVENTO_IN_IDS,idsPrezzoSettoreEvento,BigliettoMicroDTO[].class);
    }

}
