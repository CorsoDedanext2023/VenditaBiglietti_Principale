package it.dedagroup.venditabiglietti.principal.util;

import it.dedagroup.venditabiglietti.principal.dto.response.EventoMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.response.SettoreMicroDTO;
import it.dedagroup.venditabiglietti.principal.mapper.EventoMapper;
import it.dedagroup.venditabiglietti.principal.mapper.LuogoMapper;
import it.dedagroup.venditabiglietti.principal.mapper.PrezzoSettoreEventoMapper;
import it.dedagroup.venditabiglietti.principal.service.EventoServiceDef;
import it.dedagroup.venditabiglietti.principal.service.LuogoServiceDef;
import it.dedagroup.venditabiglietti.principal.service.PrezzoSettoreEventoServiceDef;
import it.dedagroup.venditabiglietti.principal.service.SettoreServiceDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Contatore {

    @Autowired
    EventoServiceDef eventoService;
    @Autowired
    EventoMapper eventoMapper;
    @Autowired
    PrezzoSettoreEventoServiceDef prezzoSettoreEventoService;
    @Autowired
    PrezzoSettoreEventoMapper prezzoSettoreEventoMapper;
    @Autowired
    SettoreServiceDef settoreService;
    @Autowired
    LuogoServiceDef luogoService;
    @Autowired
    LuogoMapper luogoMapper;



    public int contaCapienzaSingola(EventoMicroDTO evento){
        List<SettoreMicroDTO> settoriLuogo=settoreService.findAllByIdLuogo(evento.getIdLuogo());
        return settoriLuogo.stream().mapToInt(SettoreMicroDTO::getCapienza).sum();
    }

    public List<Integer> contaCapienzaLista(List<EventoMicroDTO> eventi){
        return eventi.stream().map(this::contaCapienzaSingola).toList();
    }

}
