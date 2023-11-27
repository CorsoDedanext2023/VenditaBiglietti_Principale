package it.dedagroup.venditabiglietti.principal.service;

import java.util.List;

import it.dedagroup.venditabiglietti.principal.dto.response.EventoMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.request.EventiFiltratiDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.EventiFiltratiDTOResponse;
import it.dedagroup.venditabiglietti.principal.model.Evento;


public interface EventoServiceDef {

	void eliminaEvento(long id);

    public List<EventoMicroDTO> trovaEventiFuturi();
}
