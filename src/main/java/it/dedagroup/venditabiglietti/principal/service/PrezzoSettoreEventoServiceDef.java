package it.dedagroup.venditabiglietti.principal.service;

import it.dedagroup.venditabiglietti.principal.dto.request.ModifyPSEDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.PrezzoSettoreEventoMicroDTO;
import it.dedagroup.venditabiglietti.principal.model.PrezzoSettoreEvento;

import java.util.List;


public interface PrezzoSettoreEventoServiceDef {

    PrezzoSettoreEvento findById(Long idPrezzoSettoreEvento);

    PrezzoSettoreEventoMicroDTO findPSEById(Long idPrezzoSettoreEvento);

    List<PrezzoSettoreEvento> findByEventiIds(List<Long> ids);
    List<PrezzoSettoreEventoMicroDTO> findByEventiIdsList(List<Long> ids);
    List<PrezzoSettoreEvento> findAllByIdEvento(long idEvento);

    List<PrezzoSettoreEventoMicroDTO> findAllPSEByIdEvento(long idEvento);

    void modificaEvento(long idPrezzoSettoreEvento, long idEvento);

    void modificaSettore(long idPrezzoSettoreEvento, long idSettore);

    void modificaPrezzo(long idSettore, long idEvento, double prezzo);
}
