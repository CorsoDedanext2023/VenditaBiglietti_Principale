package it.dedagroup.venditabiglietti.principal.serviceimpl;


import java.util.List;

import it.dedagroup.venditabiglietti.principal.dto.response.EventoMicroDTO;
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

}
