package it.dedagroup.venditabiglietti.principal.facade;

import it.dedagroup.venditabiglietti.principal.dto.request.*;
import it.dedagroup.venditabiglietti.principal.dto.response.*;
import it.dedagroup.venditabiglietti.principal.mapper.*;
import it.dedagroup.venditabiglietti.principal.model.*;
import it.dedagroup.venditabiglietti.principal.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class VenditoreFacade implements GeneralCallService{
    //TODO controllare su github i vari url dei microservizi
    //TODO discutere della creazione di un service layer dove utilizzare le chiamate ai microservizi
    private final LuogoServiceDef luogoServiceDef;
    private final UtenteServiceDef utenteServiceDef;
    private final BigliettoServiceDef bigliettoServiceDef;
    private final ManifestazioneServiceDef manifestazioneServiceDef;
    private final CategoriaServiceDef categoriaServiceDef;
    private final EventoServiceDef eventoServiceDef;
    private final BigliettiMapper bigliettiMapper;
    private final ManifestazioneMapper manifestazioneMapper;
    private final EventoMapper eventoMapper;
    private final LuogoMapper luogoMapper;
    private final PrezzoSettoreEventoMapper prezzoSettoreEventoMapper;
    public final String EVENTO_PATH = "http://localhost:8081/evento";
    public final String CATEGORIA_PATH = "http://localhost:8082/categoria";
    public final String SETTORE_PATH = "http://localhost:8083/settore";
    public final String MANIFESTAZIONE_PATH = "http://localhost:8084/manifestazione";
    public final String PREZZO_SETTORE_EVENTO_PATH = "http://localhost:8086/prezzoSettoreEvento";

    public final String LUOGO_PATH = "http://localhost:8088/biglietto";
    /*

    public ManifestazioneDTOResponse addManifestazione(AddManifestazioneDTORequest request, Utente u){
        if (!u.getRuolo().equals(Ruolo.VENDITORE))throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore l'utente non ha i permessi");
        ManifestazioneMicroDTO m=manifestazioneServiceDef.findByNome(request.getNome());
        if(m!=null) throw new ResponseStatusException(HttpStatusCode.valueOf(400),"Manifestazione già esistente!");
        CategoriaMicroDTO c=categoriaServiceDef.findById(request.getIdCategoria());
        return manifestazioneMapper.fromMicroDTOtoManifestazioneDTOResponse(manifestazioneServiceDef.save(request), u);
    }
    //TODO Modificare l'implementazione del filtro luoghi
    public List<LuogoMicroDTO> findAllLuogo(){
        return luogoServiceDef.findAll();
    }

    public EventoDTOResponse addEvento(AddEventoDTORequest request, Utente u) {
        if(u.getRuolo()!=Ruolo.VENDITORE) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'utente non è un venditore");
        ManifestazioneMicroDTO m = callGet(MANIFESTAZIONE_PATH+request.getIdManifestazione(), null, ManifestazioneMicroDTO.class);
        if (m.getIdUtente()!=u.getId())throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'utente che effettua la richiesta non e' l'organizzatore della manifestazione: "+m.getNome());
        LuogoMicroDTO l = callGet(LUOGO_PATH+"findById/"+request.getIdLuogo(), null, LuogoMicroDTO.class);
        return callPost(EVENTO_PATH+"salva",request,EventoDTOResponse.class);
    }

    public EventoDTOResponse deleteEvento(long idEvento, Utente u) {
        if(u.getRuolo()!=Ruolo.VENDITORE) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'utente non è un venditore");
        EventoMicroDTO e = callGet(EVENTO_PATH+"findById"+idEvento, null, EventoMicroDTO.class);
        ManifestazioneMicroDTO m = callGet(MANIFESTAZIONE_PATH+e.getIdManifestazione(), null, ManifestazioneMicroDTO.class);
        if (m.getIdUtente()!=u.getId())throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'utente che effettua la richiesta non e' l'organizzatore della manifestazione: "+m.getNome());
        return callPut(EVENTO_PATH+"delete",idEvento,EventoDTOResponse.class);
    }


   public VisualizzaEventoManifestazioneDTOResponse visualizzaEventiOrganizzati(long idManifestazione, Utente u) {
       if(u.getRuolo()!=Ruolo.VENDITORE) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'utente non è un venditore");
        ManifestazioneMicroDTO m = callGet(MANIFESTAZIONE_PATH + idManifestazione,null, ManifestazioneMicroDTO.class);
       if (m.getIdUtente()!=u.getId())throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'utente che effettua la richiesta non e' l'organizzatore della manifestazione: "+m.getNome());
        List<EventoMicroDTO> listaEventi = callGetForList("/trovaEventiDiManifestazione" + m.getId(), null, EventoMicroDTO[].class);
        VisualizzaEventoManifestazioneDTOResponse vemDTO = new VisualizzaEventoManifestazioneDTOResponse();
        Map<String, String> eventiManifestazione = new HashMap<>();
        List<LuogoMicroDTO> listaLuoghi = listaEventi.stream().map(e -> callGet(LUOGO_PATH + "/findById/" + e.getIdLuogo(),  null, LuogoMicroDTO.class)).toList();
        for (int i = 0; i < listaLuoghi.size(); i++) {
        	eventiManifestazione.put(listaEventi.get(i).getDescrizione(), listaLuoghi.get(i).getRiga1());
        }
        vemDTO.setNomeManifestazione(m.getNome());
        vemDTO.setNomeOrganizzatore(u.getNome());
        vemDTO.setEventiManifestazione(eventiManifestazione);
        return vemDTO;
    }

    public StatisticheManifestazioneDTOResponse statisticheBigliettiPerManifestazione(long id_manifestazione, Utente u) {
        if (!u.getRuolo().equals(Ruolo.VENDITORE))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore l'utente non ha i permessi");
        ManifestazioneMicroDTO microDTO = callGet(MANIFESTAZIONE_PATH + "/find/id/" + id_manifestazione, null, ManifestazioneMicroDTO.class);
        if (u.getId() != microDTO.getIdUtente()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore inserire una manifestazione organizzata da: "+u.getEmail());
        Manifestazione manifestazione = manifestazioneMapper.toManifestazione(microDTO, u);
        List<EventoMicroDTO> eventiManifestazione = callGetForList(EVENTO_PATH + "/find/all/microDTO/id" + microDTO.getId(), null, EventoMicroDTO[].class);
        if (eventiManifestazione.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento presente nella manifestazione: "+manifestazione.getNome());
        List<Long> idLuoghi = eventiManifestazione.stream().map(EventoMicroDTO::getIdLuogo).toList();
        List<LuogoMicroDTO> luoghiMicroDTO = callPostForList(LUOGO_PATH+"/findAllByIds", idLuoghi, LuogoMicroDTO[].class);
        if (luoghiMicroDTO.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun luogo assegnato ai singoli eventi");
        List<SettoreMicroDTO> settoreMicroDTO = callPostForList(SETTORE_PATH+"/findAllByListIdLuogo", idLuoghi, SettoreMicroDTO[].class);
        if (settoreMicroDTO.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun luogo assegnato ai singoli settori");
        List<Long> idsEventi = eventiManifestazione.stream().map(EventoMicroDTO::getId).toList();
        List<PrezzoSettoreEventoMicroDTO> psePerEventi = callPostForList(PREZZO_SETTORE_EVENTO_PATH+"/prezzi-settore-evento/ids-evento",idsEventi, PrezzoSettoreEventoMicroDTO[].class);
        if (psePerEventi.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento assegnato ai singoli prezzoSettoreEvento");
        List<Luogo> luoghi = luogoMapper.toLuogoList(luoghiMicroDTO, settoreMicroDTO);
        List<Evento> eventi = eventoMapper.toEventoList(eventiManifestazione, manifestazione, luoghi, psePerEventi);
        List<Long> idsPrezzoSettoreEvento = psePerEventi.stream().map(PrezzoSettoreEventoMicroDTO::getId).toList();
        List<BigliettoMicroDTO> bigliettoMicroDTO = bigliettoServiceDef.findAllByIdPrezzoSettoreEventoIn(idsPrezzoSettoreEvento);
        bigliettiMapper.toBigliettoList(bigliettoMicroDTO, eventi, "");
        return calcolaStatistiche(manifestazione);
    }

    private StatisticheManifestazioneDTOResponse calcolaStatistiche(Manifestazione m) {
        StatisticheManifestazioneDTOResponse response = new StatisticheManifestazioneDTOResponse();
        response.setNomeManifestazione(m.getNome());
        response.setProfittoEventiDellaManifestazione(m.getEventi().stream().map(this::calcolaSingoloEvento).toList());
        return response;
    }

    public DatiEventiDTOResponse statisticheBigliettiPerEvento(long id_evento, Utente u) {
        if (!u.getRuolo().equals(Ruolo.VENDITORE))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore l'utente non ha i permessi");
        EventoMicroDTO eventoDTO = callGet(EVENTO_PATH+"/find/id/"+id_evento, null, EventoMicroDTO.class);
        ManifestazioneMicroDTO manifestazioneDTO = callGet(MANIFESTAZIONE_PATH+"/find/id/"+eventoDTO.getIdManifestazione(),null, ManifestazioneMicroDTO.class);
        if (manifestazioneDTO.getIdUtente() != u.getId())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore inserire un id evento corrispondente alla manifestazione: " + manifestazioneDTO.getNome());
        LuogoMicroDTO luogoDTO = callGet(LUOGO_PATH+"/findById/"+eventoDTO.getIdLuogo(), null, LuogoMicroDTO.class);
        List<SettoreMicroDTO> settoriDTO = callGetForList(SETTORE_PATH+"/findByIdLuogo?idLuogo="+luogoDTO.getId(), null, SettoreMicroDTO[].class);
        if (settoriDTO.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun settore trovato con id luogo: "+luogoDTO.getId());
        List<PrezzoSettoreEventoMicroDTO> pseDTO = callGetForList(PREZZO_SETTORE_EVENTO_PATH+"/prezzi-settore-evento/ids-evento", List.of(eventoDTO.getId()), PrezzoSettoreEventoMicroDTO[].class);
        if (pseDTO.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun prezzo settore eventi trovato tramite id evento: "+eventoDTO.getId());
        Luogo luogo = luogoMapper.toLuogo(luogoDTO, settoriDTO);
        Manifestazione manifestazione = manifestazioneMapper.toManifestazione(manifestazioneDTO, u);
        Evento evento = eventoMapper.toEvento(eventoDTO, manifestazione, luogo, pseDTO);
        return calcolaSingoloEvento(evento);
    }

    private DatiEventiDTOResponse calcolaSingoloEvento(Evento e) {
        DatiEventiDTOResponse d = new DatiEventiDTOResponse();
        d.setDescrizione(e.getDescrizione());
        d.setViaLuogo(e.getLuogo().getRiga1());
        d.setSettoriPerLuogo(e.getPrezziSettoreEvento().stream().map(this::calcolaSettoreSingoloLuogo).toList());
        return d;
    }

    private SettoriPerSingoloLuogo calcolaSettoreSingoloLuogo(PrezzoSettoreEvento pse) {
        SettoriPerSingoloLuogo s = new SettoriPerSingoloLuogo();
        s.setBigliettiTotali(pse.getSettore().getCapienza());
        s.setSettore(pse.getSettore().getNome());
        s.setPrezzoAttuale(pse.getPrezzo());
        s.setBigliettiComprati(pse.getBiglietti().size());
        s.setGuadagnoBiglietti(pse.getBiglietti().stream().mapToDouble(Biglietto::getPrezzo).sum());
        return s;
    }

    public PseDTOResponse setPrezzoSettoreEvento(PrezzoSettoreEventoDTORequest request, Utente utente) {
        if (!utente.getRuolo().equals(Ruolo.VENDITORE)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore, l'utente non ha i permessi");
        EventoMicroDTO eventoDTO = callPost(EVENTO_PATH+"/find/id" + request.getIdEvento(), null, EventoMicroDTO.class);
        ManifestazioneMicroDTO manifestazioneDTO = callPost(MANIFESTAZIONE_PATH+"/find/id"+eventoDTO.getIdManifestazione(), null, ManifestazioneMicroDTO.class);
        if (manifestazioneDTO.getIdUtente() != utente.getId()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore annullata la modifica del prezzo settore evento");
        SettoreMicroDTO settoreDTO = callGet(SETTORE_PATH + "/find/id" + request.getIdSettore(),null, SettoreMicroDTO.class);
        PrezzoSettoreEventoMicroDTO pseDTO = callPost(PREZZO_SETTORE_EVENTO_PATH + "/find/id" + request.getIdPse(), null, PrezzoSettoreEventoMicroDTO.class);

        callPost(PREZZO_SETTORE_EVENTO_PATH + "/prezzi-settore-evento/modifica-evento?idPse="+ pseDTO.getId() + "&idEvento="+ eventoDTO.getId(),null,Void.class);
        callPost(PREZZO_SETTORE_EVENTO_PATH + "/prezzi-settore-evento/modifica-settore?idPse=" + pseDTO.getId() + "&idSettore=" + settoreDTO.getId(), null, Void.class);
        callGet(PREZZO_SETTORE_EVENTO_PATH + "/prezzi-settore-evento/modifica-prezzo",  request, SettoreMicroDTO.class);

        PrezzoSettoreEventoMicroDTO pseDTOmodificato = callPost(PREZZO_SETTORE_EVENTO_PATH+"/find/id"+pseDTO.getId(),null, PrezzoSettoreEventoMicroDTO.class);
        return prezzoSettoreEventoMapper.toPseDTOResponse(pseDTOmodificato,eventoDTO.getDescrizione(),settoreDTO.getNome());
    }

     */
}

