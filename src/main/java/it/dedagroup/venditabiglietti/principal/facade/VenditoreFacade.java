package it.dedagroup.venditabiglietti.principal.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.dedagroup.venditabiglietti.principal.dto.request.AddEventoDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.AddManifestazioneDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.ModifyPSEDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.AddEventoResponse;
import it.dedagroup.venditabiglietti.principal.dto.response.BigliettoMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.response.CategoriaMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.response.DatiEventiDTOResponse;
import it.dedagroup.venditabiglietti.principal.dto.response.EventoMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.response.LuogoMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.response.ManifestazioneMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.response.PrezzoSettoreEventoMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.response.PseDTOResponse;
import it.dedagroup.venditabiglietti.principal.dto.response.SettoreMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.response.SettoriPerSingoloLuogo;
import it.dedagroup.venditabiglietti.principal.dto.response.StatisticheManifestazioneDTOResponse;
import it.dedagroup.venditabiglietti.principal.dto.response.VisualizzaEventoManifestazioneDTOResponse;
import it.dedagroup.venditabiglietti.principal.mapper.BigliettiMapper;
import it.dedagroup.venditabiglietti.principal.mapper.EventoMapper;
import it.dedagroup.venditabiglietti.principal.mapper.LuogoMapper;
import it.dedagroup.venditabiglietti.principal.mapper.ManifestazioneMapper;
import it.dedagroup.venditabiglietti.principal.mapper.PrezzoSettoreEventoMapper;
import it.dedagroup.venditabiglietti.principal.model.Biglietto;
import it.dedagroup.venditabiglietti.principal.model.Evento;
import it.dedagroup.venditabiglietti.principal.model.Luogo;
import it.dedagroup.venditabiglietti.principal.model.Manifestazione;
import it.dedagroup.venditabiglietti.principal.model.PrezzoSettoreEvento;
import it.dedagroup.venditabiglietti.principal.model.Ruolo;
import it.dedagroup.venditabiglietti.principal.model.Settore;
import it.dedagroup.venditabiglietti.principal.model.Utente;
import it.dedagroup.venditabiglietti.principal.service.BigliettoServiceDef;
import it.dedagroup.venditabiglietti.principal.service.CategoriaServiceDef;
import it.dedagroup.venditabiglietti.principal.service.EventoServiceDef;
import it.dedagroup.venditabiglietti.principal.service.GeneralCallService;
import it.dedagroup.venditabiglietti.principal.service.LuogoServiceDef;
import it.dedagroup.venditabiglietti.principal.service.ManifestazioneServiceDef;
import it.dedagroup.venditabiglietti.principal.service.PrezzoSettoreEventoServiceDef;
import it.dedagroup.venditabiglietti.principal.service.SettoreServiceDef;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VenditoreFacade implements GeneralCallService{
    //TODO controllare su github i vari url dei microservizi
    //TODO discutere della creazione di un service layer dove utilizzare le chiamate ai microservizi
    private final LuogoServiceDef luogoServiceDef;
    private final BigliettoServiceDef bigliettoServiceDef;
    private final ManifestazioneServiceDef manifestazioneServiceDef;
    private final CategoriaServiceDef categoriaServiceDef;
    private final EventoServiceDef eventoServiceDef;
    private final SettoreServiceDef settoreServiceDef;
    private final PrezzoSettoreEventoServiceDef prezzoSettoreEventoServiceDef;

    private final BigliettiMapper bigliettiMapper;
    private final ManifestazioneMapper manifestazioneMapper;
    private final EventoMapper eventoMapper;
    private final LuogoMapper luogoMapper;
    private final PrezzoSettoreEventoMapper prezzoSettoreEventoMapper;

    

    public String addManifestazione(AddManifestazioneDTORequest request, Utente u){
        if (!u.getRuolo().equals(Ruolo.VENDITORE))throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore l'utente non ha i permessi");
        try {
            ManifestazioneMicroDTO m=manifestazioneServiceDef.findByNome(request.getNome());
            if(m!=null) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Manifestazione già esistente!");
        } catch (ResponseStatusException e) {
            if (e.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)){
                CategoriaMicroDTO c=categoriaServiceDef.findById(request.getIdCategoria());
                request.setNome(request.getNome());
                request.setIdCategoria(c.getId());
                request.setIdUtente(u.getId());
            } else {
                throw e;
            }
        }
        return manifestazioneServiceDef.save(request);
    }
    
    //TODO Modificare l'implementazione del filtro luoghi
    public List<LuogoMicroDTO> filtraLuoghiMap(Map<String, String> mapLuoghi, Utente u){
        if(u.getRuolo()!=Ruolo.VENDITORE) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore l'utente non ha i permessi");
    	if(mapLuoghi==null||mapLuoghi.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nessun Parametro inserito");
    	return luogoServiceDef.filtraLuoghiMap(mapLuoghi);
    }

    public AddEventoResponse addEvento(AddEventoDTORequest request, Utente u) {
        if(u.getRuolo()!=Ruolo.VENDITORE) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'utente non è un venditore");
        ManifestazioneMicroDTO m = manifestazioneServiceDef.findById(request.getIdManifestazione());
        if (m.getIdUtente()!=u.getId())throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'utente che effettua la richiesta non e' l'organizzatore della manifestazione: "+m.getNome());
        LuogoMicroDTO l = luogoServiceDef.findLuogoById(request.getIdLuogo());
        eventoServiceDef.save(request);
        EventoMicroDTO eventoDTO = eventoServiceDef.findByDescrizione(request.getDescrizione());
        return eventoMapper.toAddEventoResponse(eventoDTO, m.getNome(), l.getRiga1());
    }

    public void deleteEvento(long idEvento, Utente u) {
        if(u.getRuolo()!=Ruolo.VENDITORE) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'utente non è un venditore");
        EventoMicroDTO e = eventoServiceDef.findById(idEvento);
        ManifestazioneMicroDTO m = manifestazioneServiceDef.findById(e.getIdManifestazione());
        if (m.getIdUtente()!=u.getId())throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'utente che effettua la richiesta non e' l'organizzatore della manifestazione: "+m.getNome());
        eventoServiceDef.deleteEvento(idEvento);
    }


   public VisualizzaEventoManifestazioneDTOResponse visualizzaEventiOrganizzati(long idManifestazione, Utente u) {
        if(u.getRuolo()!=Ruolo.VENDITORE) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'utente non è un venditore");
        ManifestazioneMicroDTO m = manifestazioneServiceDef.findById(idManifestazione);
        if (m.getIdUtente()!=u.getId())throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'utente che effettua la richiesta non e' l'organizzatore della manifestazione: "+m.getNome());
        
        List<EventoMicroDTO> listaEventi = eventoServiceDef.findAllByManifestazioneId(idManifestazione);
        VisualizzaEventoManifestazioneDTOResponse vemDTO = new VisualizzaEventoManifestazioneDTOResponse();
        Map<String, String> eventiManifestazione = new HashMap<>();
        List<LuogoMicroDTO> listaLuoghi = listaEventi.stream().map(e -> luogoServiceDef.findLuogoById(idManifestazione)).toList();
        for (int i = 0; i < listaLuoghi.size(); i++) {
         	eventiManifestazione.put(listaEventi.get(i).getDescrizione(), listaLuoghi.get(i).getRiga1());
        }
        vemDTO.setNomeManifestazione(m.getNome());
        vemDTO.setNomeOrganizzatore(u.getNome());
        vemDTO.setEventiManifestazione(eventiManifestazione);
        return vemDTO;
    }

    public StatisticheManifestazioneDTOResponse statisticheBigliettiPerManifestazione(long id_manifestazione, Utente u) {
        if (!u.getRuolo().equals(Ruolo.VENDITORE)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore l'utente non ha i permessi");
        ManifestazioneMicroDTO microDTO = manifestazioneServiceDef.findById(id_manifestazione);
        if (u.getId() != microDTO.getIdUtente()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore inserire una manifestazione organizzata da: "+u.getEmail());
        Manifestazione manifestazione = manifestazioneMapper.toManifestazione(microDTO, u);
        List<EventoMicroDTO> eventiManifestazione = eventoServiceDef.findAllByManifestazioneId(microDTO.getId());
        if (eventiManifestazione.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun evento presente nella manifestazione: "+manifestazione.getNome());
        List<Long> idLuoghi = eventiManifestazione.stream().map(EventoMicroDTO::getIdLuogo).toList();
        List<LuogoMicroDTO> luoghiMicroDTO = luogoServiceDef.findAllByIds(idLuoghi);
        if (luoghiMicroDTO.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun luogo assegnato ai singoli eventi");
        List<SettoreMicroDTO> settoreMicroDTO = settoreServiceDef.findAllByListIdsLuogo(idLuoghi);
        if (settoreMicroDTO.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun luogo assegnato ai singoli settori");
        List<Long> idsEventi = eventiManifestazione.stream().map(EventoMicroDTO::getId).toList();
        List<PrezzoSettoreEventoMicroDTO> psePerEventi = prezzoSettoreEventoServiceDef.findByEventiIdsList(idsEventi);
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
        if (!u.getRuolo().equals(Ruolo.VENDITORE)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore l'utente non ha i permessi");
        EventoMicroDTO eventoDTO = eventoServiceDef.findById(id_evento);
        ManifestazioneMicroDTO manifestazioneDTO = manifestazioneServiceDef.findById(eventoDTO.getIdManifestazione());
        if (manifestazioneDTO.getIdUtente() != u.getId()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore inserire un id evento corrispondente alla manifestazione: " + manifestazioneDTO.getNome());
        LuogoMicroDTO luogoDTO = luogoServiceDef.findLuogoById(eventoDTO.getId());
        List<SettoreMicroDTO> settoriDTO = settoreServiceDef.findAllByIdLuogo(luogoDTO.getId());
        if (settoriDTO.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nessun settore trovato con id luogo: "+luogoDTO.getId());
        List<PrezzoSettoreEventoMicroDTO> pseDTO = prezzoSettoreEventoServiceDef.findAllPSEByIdEvento(eventoDTO.getId());
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
        if (pse==null)pse = new PrezzoSettoreEvento();
        if (pse.getSettore()==null) pse.setSettore(new Settore());
        int bigliettiDisponibili = pse.getSettore().getCapienza() - pse.getBiglietti().size();
        s.setBigliettiTotali(bigliettiDisponibili);
        s.setSettore(pse.getSettore().getNome());
        s.setPrezzoAttuale(pse.getPrezzo());
        s.setBigliettiComprati(pse.getBiglietti().size());
        s.setGuadagnoBiglietti(pse.getBiglietti().stream().mapToDouble(Biglietto::getPrezzo).sum());
        return s;
    }

    public PseDTOResponse setPrezzoSettoreEvento(ModifyPSEDTORequest request, Utente utente) {
        if (!utente.getRuolo().equals(Ruolo.VENDITORE)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore, l'utente non ha i permessi");
        EventoMicroDTO eventoDTO = eventoServiceDef.findById(request.getIdEvento());
        ManifestazioneMicroDTO manifestazioneDTO = manifestazioneServiceDef.findById(eventoDTO.getIdManifestazione());
        if (manifestazioneDTO.getIdUtente() != utente.getId()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Errore annullata la modifica del prezzo settore evento");
        SettoreMicroDTO settoreDTO = settoreServiceDef.findById(request.getIdSettore());
        PrezzoSettoreEventoMicroDTO pseDTO = prezzoSettoreEventoServiceDef.findPSEById(request.getIdPse());

        prezzoSettoreEventoServiceDef.modificaEvento(pseDTO.getId(),eventoDTO.getId());
        prezzoSettoreEventoServiceDef.modificaSettore(pseDTO.getId(),settoreDTO.getId());
        prezzoSettoreEventoServiceDef.modificaPrezzo(settoreDTO.getId(),eventoDTO.getId(),request.getPrezzo());

        PrezzoSettoreEventoMicroDTO pseDTOmodificato = prezzoSettoreEventoServiceDef.findPSEById(pseDTO.getId());
        return prezzoSettoreEventoMapper.toPseDTOResponse(pseDTOmodificato,eventoDTO.getDescrizione(),settoreDTO.getNome());
    }

     
}

