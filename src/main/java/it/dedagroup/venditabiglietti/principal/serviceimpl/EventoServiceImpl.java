package it.dedagroup.venditabiglietti.principal.serviceimpl;


import java.util.List;

import it.dedagroup.venditabiglietti.principal.dto.request.EventiCriteriaDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.EventoMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.request.AddEventoDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.EventiFiltratiDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.EventiFiltratiDTOResponse;
import org.springframework.stereotype.Service;

import it.dedagroup.venditabiglietti.principal.service.EventoServiceDef;
import it.dedagroup.venditabiglietti.principal.service.GeneralCallService;

@Service
public class EventoServiceImpl implements EventoServiceDef, GeneralCallService{
private String servicePath="http://localhost:8081/evento/";

	
	@Override
	public void eliminaEvento(long id) {
		callPost(servicePath+"delete/"+id, id, String.class);
	}

  @Override
    public List<EventoMicroDTO> trovaEventiFuturi() {
        String mioPath=servicePath+"trovaEventiFuturi";
        List<EventoMicroDTO> listaEventiFuturi =callGetForList(mioPath,null, EventoMicroDTO[].class);
        return listaEventiFuturi;
        
    }

    @Override
    public List<EventoMicroDTO> findAllByManifestazioneId(long id_manifestazione) {
        return callGetForList(servicePath + "/trovaEventiDiManifestazione/" + id_manifestazione, null, EventoMicroDTO[].class);
    }

    @Override
    public EventoMicroDTO findById(long idEvento) {
        return callGet(servicePath+"id/"+idEvento,idEvento,EventoMicroDTO.class);
    }

    @Override
    public EventoMicroDTO findByDescrizione(String descrizione) {
        return callPost(servicePath+"trovaPerDescrizione?descrizione="+descrizione,descrizione,EventoMicroDTO.class);
    }

    @Override
	public void save(AddEventoDTORequest request) {
		callPost(servicePath+"salva", request, Void.class);
	}

	@Override
	public void deleteEvento(long idEvento) {
		callPost(servicePath+"/cancella/"+idEvento, null, EventoMicroDTO.class);
	}

    @Override
    public List<EventoMicroDTO> criteriaEventiFiltrati(EventiCriteriaDTORequest request) {
        return callPostForList(servicePath + "filtraEventi", request, EventoMicroDTO[].class);
    }

}
