package it.dedagroup.venditabiglietti.principal.mapper;

import it.dedagroup.venditabiglietti.principal.dto.response.PrezzoSettoreEventoMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.response.PseDTOResponse;
import it.dedagroup.venditabiglietti.principal.model.Evento;
import it.dedagroup.venditabiglietti.principal.model.PrezzoSettoreEvento;
import it.dedagroup.venditabiglietti.principal.model.Settore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PrezzoSettoreEventoMapper {
    public PrezzoSettoreEvento toPrezzoSettoreEvento(PrezzoSettoreEventoMicroDTO pseDTO, Evento e, Settore s){
        PrezzoSettoreEvento pse = new PrezzoSettoreEvento();
        pse.setId(pseDTO.getId());
        pse.setPrezzo(pseDTO.getPrezzo());
        pse.setCancellato(pseDTO.isCancellato());
        pse.setBiglietti(new ArrayList<>());
        pse.setVersion(pseDTO.getVersion());
        if(e!=null) pse.setEvento(e);
        if(pse!=null) e.addPrezzoSettoreEvento(pse);
        if(s!=null) pse.setSettore(s);
        if(s!=null) s.addPrezzoSettoreEvento(pse);
        return pse;
    }

    public PseDTOResponse toPseDTOResponse(PrezzoSettoreEventoMicroDTO pseDTO, String descrizioneEvento, String nomeSettore){
        PseDTOResponse response = new PseDTOResponse();
        response.setId(pseDTO.getId());
        response.setDescrizioneEvento(descrizioneEvento);
        response.setNomeSettore(nomeSettore);
        response.setPrezzo(pseDTO.getPrezzo());
        return response;
    }

    public List<PrezzoSettoreEvento> toList(List<PrezzoSettoreEventoMicroDTO> pseDTO, List<Evento> e, List<Settore> s){
        return pseDTO.stream()
                .map(pse -> this.toPrezzoSettoreEvento(
                    pse,
                        e.stream().filter(evento -> evento.getId() == pse.getIdEvento()).findFirst().orElse(null),
                        s.stream().filter(settore -> settore.getId() == pse.getIdSettore()).findFirst().orElse(null)
                        )
                ).toList();
    }
}
