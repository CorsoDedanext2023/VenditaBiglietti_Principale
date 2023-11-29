package it.dedagroup.venditabiglietti.principal.mapper;

import it.dedagroup.venditabiglietti.principal.dto.request.AddEventoDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.*;
import it.dedagroup.venditabiglietti.principal.model.Evento;
import it.dedagroup.venditabiglietti.principal.model.Luogo;
import it.dedagroup.venditabiglietti.principal.model.Manifestazione;
import it.dedagroup.venditabiglietti.principal.model.PrezzoSettoreEvento;
import it.dedagroup.venditabiglietti.principal.dto.response.*;
import it.dedagroup.venditabiglietti.principal.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventoMapper {

    @Autowired
    PrezzoSettoreEventoMapper mapper;

    public AddEventoResponse toAddEventoResponse(EventoMicroDTO eventoDTO, String nomeManifestazione, String viaLuogo){
        AddEventoResponse response = new AddEventoResponse();
        response.setId(eventoDTO.getId());
        response.setData(eventoDTO.getData());
        response.setOra(eventoDTO.getOra());
        response.setDescrizione(eventoDTO.getDescrizione());
        response.setNomeManifestazione(nomeManifestazione);
        response.setViaLuogo(viaLuogo);
        return response;
    }
    public EventoDTOResponse toEventoDTOResponse(Evento ev){
        EventoDTOResponse evDTOResp = new EventoDTOResponse();
        evDTOResp.setId(ev.getId());
        evDTOResp.setData(ev.getData());
        evDTOResp.setOra(ev.getOra());
        evDTOResp.setDescrizione(ev.getDescrizione());
        evDTOResp.setIdLuogo(ev.getLuogo().getId());
        evDTOResp.setIdManifestazione(ev.getManifestazione().getId());
        return evDTOResp;
    }

    public EventoDTOResponse fromAddEventoDTORequestToEventoDTOResponse(AddEventoDTORequest request, Utente u) {
    	EventoDTOResponse eventoDTO = new EventoDTOResponse();
    	eventoDTO.setData(LocalDate.parse(request.getData()));
    	eventoDTO.setOra(LocalTime.parse(request.getOra()));
    	eventoDTO.setDescrizione(request.getDescrizione());
    	eventoDTO.setIdManifestazione(request.getIdManifestazione());
    	eventoDTO.setIdLuogo(request.getIdLuogo());
    	return eventoDTO;
    }

    public EventoDTOResponse fromMicroDTOtoEventoDTOResponse(EventoMicroDTO request) {
    	EventoDTOResponse eventoDTO = new EventoDTOResponse();
    	eventoDTO.setId(request.getId());
    	eventoDTO.setData(request.getData());
    	eventoDTO.setOra(request.getOra());
    	eventoDTO.setDescrizione(request.getDescrizione());
    	eventoDTO.setIdManifestazione(request.getIdManifestazione());
    	eventoDTO.setIdLuogo(request.getIdLuogo());
    	return eventoDTO;
    }

    public List<EventoDTOResponse> toEventoDTOResponseList(List<Evento> eventi){
        return eventi.stream().map(this::toEventoDTOResponse).toList();
    }

    public List<Evento> toEventoList(List<EventoMicroDTO> eventiManifestazione, Manifestazione m, List<Luogo> luoghi, List<PrezzoSettoreEventoMicroDTO> prezziMicroDTO) {
        return eventiManifestazione.stream().map(evento -> toEvento(evento,
                                                                    m,
                                                                    luoghi==null||luoghi.isEmpty()?null:luoghi.stream().filter(l->l.getId()==evento.getIdLuogo()).findFirst().get()
                                                                    ,prezziMicroDTO==null || prezziMicroDTO.isEmpty()?null:prezziMicroDTO.stream().filter(p->p.getIdEvento()==evento.getId()).toList()
                                                                    )).toList();
    }

    

    public Evento toEvento(EventoMicroDTO request, Manifestazione m, Luogo l, List<PrezzoSettoreEventoMicroDTO> pse){
        Evento e=new Evento();
        e.setId(request.getId());
        e.setData(request.getData());
        e.setOra(request.getOra());
        e.setDescrizione(request.getDescrizione());
        e.setCancellato(request.isCancellato());
        e.setManifestazione(m);
        if(e!=null) m.addEvento(e);
        if(l!=null)e.setLuogo(l);
        if(e!=null)l.addEventi(e);
        List<PrezzoSettoreEvento> p=mapper.toList(pse,List.of(e),l.getSettori());
        e.setPrezziSettoreEvento(p);
        return e;
    }

    public MostraEventiFuturiDTOResponse mostraEventiFuturiResponseBuilder (EventoMicroDTO evento,LuogoMicroDTO luogo,ManifestazioneMicroDTO manifestazione){
        MostraEventiFuturiDTOResponse response=new MostraEventiFuturiDTOResponse();
        response.setLuogoEventoRiga1(luogo.getRiga1());
        if(response.getRiga2()==null){
            response.setRiga2("");
        } else response.setRiga2(luogo.getRiga2());
        response.setNomeManifestazione(manifestazione.getNome());
        response.setComune(luogo.getComune());
        if(response.getCap()==null){
            response.setCap("");
        } else response.setCap(luogo.getCap());
        if(response.getProvincia()==null){
            response.setProvincia("");
        } else response.setProvincia(luogo.getProvincia());
        response.setNomeEvento(evento.getDescrizione());
        response.setDataEvento(evento.getData());
        response.setOrarioEvento(evento.getOra());
        return response;
    }

    public EventiFiltratiDTOResponse toEventiFiltratiDTOResponse(Evento e, Luogo l, Categoria c, Manifestazione m){
        EventiFiltratiDTOResponse response = new EventiFiltratiDTOResponse();
        response.setDataEvento(e.getData());
        response.setOraEvento(e.getOra());
        response.setDescrizioneEvento(e.getDescrizione());
        response.setComune(l.getComune());
        response.setProvincia(l.getProvincia());
        response.setNomeCategoria(c.getNome());
        response.setNomeManifestazione(m.getNome());
        return response;
    }

    public List<EventiFiltratiDTOResponse> toListOfEventiFiltratiDTOResponse(List<Evento> eventoList, List<Luogo> luogoList,
                                                                             List<Categoria> categoriaList, List<Manifestazione> manifestazioneList){
        List<EventiFiltratiDTOResponse> responseList = new ArrayList<>();
        for(int i = 0; i < eventoList.size(); i++){
            EventiFiltratiDTOResponse dtoResponse = new EventiFiltratiDTOResponse();
            dtoResponse.setDescrizioneEvento(eventoList.get(i).getDescrizione());
            dtoResponse.setDataEvento(eventoList.get(i).getData());
            dtoResponse.setOraEvento(eventoList.get(i).getOra());
            responseList.add(dtoResponse);
        }
        return responseList;
    }

    public EventiFiltratiDTOResponse toEventoFiltratoDTOResponse(MostraEventiFuturiDTOResponse inputResponse){
        EventiFiltratiDTOResponse outputResponse=new EventiFiltratiDTOResponse();
        outputResponse.setDataEvento(inputResponse.getDataEvento());
        outputResponse.setOraEvento(inputResponse.getOrarioEvento());
        outputResponse.setDescrizioneEvento(inputResponse.getNomeEvento());
        outputResponse.setProvincia(inputResponse.getProvincia());
        outputResponse.setComune(inputResponse.getComune());
        outputResponse.setNomeCategoria("");
        return outputResponse;
    }

    public List<EventiFiltratiDTOResponse> toEventiFiltratiDTOResponse (List<MostraEventiFuturiDTOResponse> listaEventiFuturiConBiglietti ){
        return listaEventiFuturiConBiglietti.stream().map(this::toEventoFiltratoDTOResponse).toList();
    }




}
