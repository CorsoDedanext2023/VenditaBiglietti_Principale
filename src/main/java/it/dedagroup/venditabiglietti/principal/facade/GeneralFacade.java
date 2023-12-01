package it.dedagroup.venditabiglietti.principal.facade;

import it.dedagroup.venditabiglietti.principal.dto.request.*;
import it.dedagroup.venditabiglietti.principal.dto.response.*;
import it.dedagroup.venditabiglietti.principal.dto.response.EventiFiltratiDTOResponse;
import it.dedagroup.venditabiglietti.principal.dto.response.EventoDTOResponse;
import it.dedagroup.venditabiglietti.principal.mapper.EventoMapper;
import it.dedagroup.venditabiglietti.principal.mapper.LuogoMapper;
import it.dedagroup.venditabiglietti.principal.mapper.LuogoMapper;
import it.dedagroup.venditabiglietti.principal.mapper.UtenteMapper;
import it.dedagroup.venditabiglietti.principal.model.*;
import it.dedagroup.venditabiglietti.principal.service.*;
import it.dedagroup.venditabiglietti.principal.util.Contatore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import it.dedagroup.venditabiglietti.principal.service.GeneralCallService;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneralFacade implements GeneralCallService {

    @Autowired
    ManifestazioneServiceDef mServ;
    @Autowired
    UtenteServiceDef uServ;
    @Autowired
    EventoServiceDef eServ;
    @Autowired
    LuogoServiceDef lServ;
    @Autowired
    EventoMapper evMap;
    @Autowired
    LuogoMapper lMap;
    @Autowired
    UtenteMapper uMap;
    @Autowired
    PrezzoSettoreEventoServiceDef pseServ;
    @Autowired
    BigliettoServiceDef bServ;
    @Autowired
    SettoreServiceDef settServ;
    @Autowired
    CategoriaServiceDef cServ;
    @Autowired
    Contatore contatoreUtils;

    public void registrazioneCliente(AggiungiUtenteDTORequest req) {
        Utente uNew = uMap.toUtenteCliente(req);
        uServ.aggiungiUtente(uNew);
    }

    public List<MostraEventiFuturiDTOResponse> trovaEventiFuturiConBiglietti() {
        List<EventoMicroDTO> eventiFuturi = eServ.trovaEventiFuturi();
        List<MostraEventiFuturiDTOResponse> listaEventiFuturiResponse = new ArrayList<>();
        for (EventoMicroDTO eventoDto : eventiFuturi) {
            MostraEventiFuturiDTOResponse response = new MostraEventiFuturiDTOResponse();
            List<PrezzoSettoreEvento> listaPsePerEvento = pseServ.findAllByIdEvento(eventoDto.getId());
            List<SettoreMicroDTO> listaSettoriPerEvento = settServ.findAllByIdLuogo(lServ.findLuogoById(eventoDto.getIdLuogo()).getId());
            int capienzaSettore = 0;
            int bigliettiVenduti = 0;
            response = evMap.mostraEventiFuturiResponseBuilder(eventoDto,
                    lServ.findLuogoById(eventoDto.getIdLuogo()),
                    mServ.findById(eventoDto.getIdManifestazione()));
            capienzaSettore = listaSettoriPerEvento.stream().mapToInt(SettoreMicroDTO::getCapienza).sum();
            bigliettiVenduti = listaPsePerEvento.stream().mapToInt(s -> bServ.countByIdPrezzoSettoreEventoAndDataAcquistoIsNotNull(s.getId())).sum();
            response.setPostiDisponibili(capienzaSettore - bigliettiVenduti);
            listaEventiFuturiResponse.add(response);
        }
        return listaEventiFuturiResponse;

    }

    public String login(LoginDTORequest request) {
        return uServ.login(request);
    }

    public Utente findById(long id) {
        return uServ.findById(id);
    }

    public List<EventiFiltratiDTOResponse> eventiFiltrati(EventiFiltratiDTORequest request) {
        List<EventoMicroDTO> eventiCriteria= eServ.criteriaEventiFiltrati(request.getRequestEventi());
        List<LuogoMicroDTO> luoghiCriteria = lServ.criteriaLuoghiFiltrati(request.getRequestLuoghi());
        List<CategoriaMicroDTO> categorieCriteria = cServ.criteriaCategorieFiltrate(request.getRequestCategoria());
        List<ManifestazioneMicroDTO> manifestazioneCriteria = mServ.criteriaManifestazioniFiltrate(request.getRequestManifestazione());
        List<EventiFiltratiDTOResponse> eventiFiltrati = new ArrayList<>();
        EventiFiltratiDTOResponse response=new EventiFiltratiDTOResponse();
        for (EventoMicroDTO evento:eventiCriteria){
            long idLuogo=evento.getIdLuogo();
            long idManifestazione=evento.getIdManifestazione();
            ManifestazioneMicroDTO manifestazioneMicroEvento=mServ.findById(idManifestazione);
            LuogoMicroDTO luogoMicroEvento=lServ.findLuogoById(idLuogo);
            long idCategoria=manifestazioneMicroEvento.getIdCategoria();
            CategoriaMicroDTO categoriaMicroEvento=cServ.findById(idCategoria);
            int capienza=contatoreUtils.contaCapienzaSingola(evento);
            if (luoghiCriteria.contains(luogoMicroEvento)&&categorieCriteria.contains(categoriaMicroEvento)&&manifestazioneCriteria.contains(manifestazioneMicroEvento)){
                response.setDataEvento(evento.getData());
                response.setOraEvento(evento.getOra());
                response.setDescrizioneEvento(evento.getDescrizione());
                response.setProvincia(luogoMicroEvento.getProvincia());
                response.setComune(luogoMicroEvento.getComune());
                response.setNomeManifestazione(manifestazioneMicroEvento.getNome());
                response.setNomeCategoria(categoriaMicroEvento.getNome());
            }
            if (response.getNomeCategoria()==null||
                    response.getNomeManifestazione()==null||
                    response.getComune()==null||
                    response.getProvincia()==null||
                    response.getDataEvento()==null||
                    response.getOraEvento()==null||
                    response.getDescrizioneEvento()==null && capienza<=0){
                response=new EventiFiltratiDTOResponse();
            } else {
                eventiFiltrati.add(response);
                response=new EventiFiltratiDTOResponse();
            }
        }
        return eventiFiltrati;
    }
}