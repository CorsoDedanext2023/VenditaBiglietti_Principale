package it.dedagroup.venditabiglietti.principal.service;

import java.util.List;

import it.dedagroup.venditabiglietti.principal.dto.response.EventoMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.request.AddEventoDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.EventiFiltratiDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.EventiFiltratiDTOResponse;
import it.dedagroup.venditabiglietti.principal.model.Evento;


public interface EventoServiceDef {

	void eliminaEvento(long id);
	void deleteEvento(long idEvento);
	
    public List<EventoMicroDTO> trovaEventiFuturi();
    public List<EventoMicroDTO> findAllByManifestazioneId(long id_manifestazione);
    
    public void save (AddEventoDTORequest request); 
    public EventoMicroDTO findById(long idEvento);

    public EventoMicroDTO findByDescrizione(String descrizione);
}
