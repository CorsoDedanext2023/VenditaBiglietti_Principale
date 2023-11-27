package it.dedagroup.venditabiglietti.principal.facade;

import it.dedagroup.venditabiglietti.principal.dto.request.AggiungiUtenteDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.EventiFiltratiDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.LoginDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.*;
import it.dedagroup.venditabiglietti.principal.dto.response.EventiFiltratiDTOResponse;
import it.dedagroup.venditabiglietti.principal.dto.response.EventoDTOResponse;
import it.dedagroup.venditabiglietti.principal.mapper.EventoMapper;
import it.dedagroup.venditabiglietti.principal.mapper.UtenteMapper;
import it.dedagroup.venditabiglietti.principal.model.*;
import it.dedagroup.venditabiglietti.principal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import it.dedagroup.venditabiglietti.principal.service.GeneralCallService;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneralFacade implements GeneralCallService{

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
    UtenteMapper uMap;
    @Autowired
    PrezzoSettoreEventoServiceDef pseServ;
    @Autowired
    BigliettoServiceDef bServ;
    @Autowired
    SettoreServiceDef settServ;

    private final String pathEvento="http://localhost:8081/evento";
    private final String pathLuogo = "http://localhost:8088/luogo";
    private final String pathCategoria = "http://localhost:8082/categoria";
    private final String pathManifestazione = "http://localhost:8084/manifestazione";

    public void registrazioneCliente(AggiungiUtenteDTORequest req){
        Utente uNew =uMap.toUtenteCliente(req);
        uServ.aggiungiUtente(uNew);
    }

    // TODO valutare sui cicli nestati se fare le lambda
    public List<MostraEventiFuturiDTOResponse> trovaEventiFuturiConBiglietti(){
        List<EventoMicroDTO> eventiFuturi = eServ.trovaEventiFuturi();
        List<MostraEventiFuturiDTOResponse> listaEventiFuturiResponse=new ArrayList<>();
        for (EventoMicroDTO eventoDto :eventiFuturi) {
            MostraEventiFuturiDTOResponse response=new MostraEventiFuturiDTOResponse();
            List<PrezzoSettoreEvento> listaPsePerEvento =pseServ.findAllByIdEvento(eventoDto.getId());
            List<SettoreMicroDTO> listaSettoriPerEvento=settServ.findAllByIdLuogo(lServ.findLuogoById(eventoDto.getIdLuogo()).getId());
            int capienzaSettore=0;
            int bigliettiVenduti=0;
            response=evMap.mostraEventiFuturiResponseBuilder(eventoDto,
                    lServ.findLuogoById(eventoDto.getIdLuogo()),
                    mServ.findById(eventoDto.getIdManifestazione()));
            capienzaSettore=listaSettoriPerEvento.stream().mapToInt(SettoreMicroDTO::getCapienza).sum();
            bigliettiVenduti=listaPsePerEvento.stream().mapToInt(s->bServ.countByIdPrezzoSettoreEventoAndDataAcquistoIsNotNull(s.getId())).sum();
            response.setPostiDisponibili(capienzaSettore-bigliettiVenduti);
            listaEventiFuturiResponse.add(response);
        }
        return listaEventiFuturiResponse;

    }
    public String login(LoginDTORequest request){
        return uServ.login(request);
    }

    public Utente findByEmail(String email){
        Utente u = uServ.findByEmail(email);
        if(u == null){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Nessun utente con email " + email);
        }
        return u;
    }

    public Utente findById(long id){
        return uServ.findById(id);
    }
    public List<Utente> findByAllId(List<Long>ids){
        return uServ.findByAllId(ids);
    }

    public List<EventiFiltratiDTOResponse> eventiFiltrati(EventiFiltratiDTORequest request) {
        List<Evento> eventiCriteria = callGetForList(pathEvento + "/filtraEventi", request.getRequestEventi(), Evento[].class);
        List<Luogo> luoghiCriteria = callGetForList(pathLuogo + "/filtroLuogo", request.getRequestLuoghi(), Luogo[].class);
        List<Categoria> categorieCriteria = callGetForList(pathCategoria + "/filtraCategorie", request.getNomeCategoria(), Categoria[].class);
        List<Manifestazione> manifestazioneCriteria = callGetForList(pathManifestazione + "/filtraManifestazioni", request.getNomeManifestazione(), Manifestazione[].class);

        return evMap.toListOfEventiFiltratiDTOResponse(eventiCriteria, luoghiCriteria, categorieCriteria, manifestazioneCriteria);
    }

}
