package it.dedagroup.venditabiglietti.principal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.dedagroup.venditabiglietti.principal.dto.request.AddManifestazioneDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.PrezzoSettoreEventoDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.*;
import it.dedagroup.venditabiglietti.principal.facade.VenditoreFacade;
import it.dedagroup.venditabiglietti.principal.model.Utente;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.dedagroup.venditabiglietti.principal.dto.request.AddEventoDTORequest;

import java.util.List;

@Tag(name = "Endpoint microservizio Evento, Luogo ,Biglietto", description = "Questo controller gestisce i microservizi elencati ed interagisce su più tabelle")
@RestController
@RequestMapping("/venditore")
@Validated
public class VenditoreController {

	@Autowired
	VenditoreFacade vendFac;

	@Operation(summary = "Metodo per aggiungere una manifestazione",description = "Questo EndPoint permette al venditore di aggiungere una manifestazione")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "La manifestazione e' stata inserita con successo nel sistema", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ManifestazioneDTOResponse.class))),
			@ApiResponse(responseCode = "403", description = "L'utente non ha i permessi per inserire la manifestazione", content = @Content(mediaType = MediaType.ALL_VALUE)),
			@ApiResponse(responseCode = "400", description = "La manifestazione inserita e' gia' esistente", content = @Content(mediaType = MediaType.ALL_VALUE)),
			@ApiResponse(responseCode = "400", description = "La categoria non e' stata trovata con l'id inserito", content = @Content(mediaType = MediaType.ALL_VALUE))
	})
	@PostMapping("/manifestazione/add")
	public ResponseEntity<ManifestazioneDTOResponse> addManifestazione(AddManifestazioneDTORequest request, UsernamePasswordAuthenticationToken upat){
		return ResponseEntity.ok(vendFac.addManifestazione(request, ((Utente)upat.getPrincipal())));
	}
	//TODO Modificare l'implementazione del filtro luoghi
	@Operation(summary = "Metodo per trovare tutti i luoghi filtrati",description = "Questo EndPoint ci permette di trovare tutti i luoghi filtrati tramite una mappa di parametri")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Viene restituita una lista di luoghi filtrati", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ManifestazioneDTOResponse.class))),
			@ApiResponse(responseCode = "404", description = "Viene lanciata un'eccezione quando la lista dei luoghi filtrati e' vuota", content = @Content(mediaType = MediaType.ALL_VALUE))
	})
	@PostMapping("/find/all/luogo")
	public ResponseEntity<List<LuogoMicroDTO>> findAllLuogo(){
		return ResponseEntity.ok(vendFac.findAllLuogo());
	}
	@Operation(summary = "Endpoint che permette di aggiungere un evento solo se l'utente è un venditore e se la manifestazione e luogo sono già presenti",
				description = "questo metodo prende in input un evento e lo aggiunge nel DB, solo dopo il verificamento del ruolo Utente(venditore) tramite upat e dopo aver verificato che "
						+ "Manifestazione e Luogo siano già esistenti")
	@ApiResponses({
		@ApiResponse(responseCode = "ACCEPTED(202)",
				description = "Evento aggiunto correttamente",
				content = @Content(mediaType = MediaType.ALL_VALUE)
	),
		@ApiResponse(responseCode = "NOTFOUND(404)",
				description = "Impossibile aggiungere Evento",
				content = @Content(mediaType = MediaType.ALL_VALUE))
	})
	@PostMapping("/evento/add")
	public ResponseEntity<EventoDTOResponse> addEvento(@RequestBody AddEventoDTORequest eventoRequest, UsernamePasswordAuthenticationToken upat){
		return ResponseEntity.ok(vendFac.addEvento(eventoRequest, ((Utente)upat.getPrincipal())));
	}
	
	@Operation(summary = "Endpoint che permette di modificare un evento solo se l'utente è un venditore e se esiste un idManifestazione",
			description = "Questo metodo ci permette di settare la booleana(isCancellato) a true, solo dopo il verificamento del ruolo Utente(venditore) tramite upat e che l'idManifestazione inserito sia già presente")
	@ApiResponses({
		@ApiResponse(responseCode = "ACCEPTED(202)",
				description = "Evento modificato correttamente",
				content = @Content(mediaType = MediaType.ALL_VALUE)
	),
		@ApiResponse(responseCode = "NOTFOUND(404)",
				description = "Impossibile modificare Evento",
				content = @Content(mediaType = MediaType.ALL_VALUE))
	})
	@PutMapping("/evento/delete/{id}")
	public ResponseEntity<EventoDTOResponse> deleteEvento(@PathVariable("id") long idManifestazione, UsernamePasswordAuthenticationToken upat){
		return ResponseEntity.ok(vendFac.deleteEvento(idManifestazione, ((Utente)upat.getPrincipal())));
	}
	
	
	
	@Operation(summary = "Endpoint che permette di visualizzare un evento solo se l'utente è un venditore e se esiste un idManifestazione",
			description = "Questo metodo ci permette di visualizzare una manifestazione(che puo avere diversi eventi) tramite idManifestazione e solo dopo aver verificato che l'Utente sia un Venditore")
	@ApiResponses({
		@ApiResponse(responseCode = "ACCEPTED(202)",
				description = "Manifestazione Trovata",
				content = @Content(mediaType = MediaType.ALL_VALUE)
	),
		@ApiResponse(responseCode = "NOTFOUND(404)",
				description = "Impossibile trovare Manifestazione",
				content = @Content(mediaType = MediaType.ALL_VALUE))
	})
	@GetMapping("/evento/visualizza/{idManifestazione}") //una manifestazione può avere diversi eventi
	public ResponseEntity<VisualizzaEventoManifestazioneDTOResponse> visualizzaEventiOrganizzati(@PathVariable long idManifestazione, UsernamePasswordAuthenticationToken upat){
		return ResponseEntity.ok(vendFac.visualizzaEventiOrganizzati(idManifestazione, ((Utente)upat.getPrincipal())));
	}
	
	
	
	
	
	
	@Operation(summary = "Metodo per controllare le statistiche dei biglietti di una manifestazione", description = "Questo EndPoint ci restituisce le statistiche dei biglietti di una manifestazione appartenente all'utente venditore che effettua la richiesta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Viene restituita una response con dentro la manifestazione i vari eventi nei rispettivi luoghi con le varie vendite dei biglietti effettuati per i singoli settori", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StatisticheManifestazioneDTOResponse.class))}),
			@ApiResponse(responseCode = "403", description = "L'utente che ha mandato la richiesta non ha i permessi", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "403", description = "L'utente che ha mandato la richiesta non e' l'organizzatore della manifestazione", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "La manifestazione non e' stata trovata con l'id inserito dall'utente venditore", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "Gli eventi non sono stati trovati con l'id della manifestazione inserito", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "I luoghi non sono stati trovati con i vari id luogo presenti negli eventi inseriti", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "I settori non sono stati trovati con i vari id luogo presenti negli eventi inseriti", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "I prezzo settore eventi non sono stati trovati con i vari id degli eventi inseriti", content = {@Content(mediaType = MediaType.ALL_VALUE)})
	})
	@PostMapping("/manifestazione/stats/biglietti/{id}")
	public ResponseEntity<StatisticheManifestazioneDTOResponse> statisticheBigliettiPerManifestazione(@PathVariable("id") long id_manifestazione,UsernamePasswordAuthenticationToken upat){
		return ResponseEntity.ok(vendFac.statisticheBigliettiPerManifestazione(id_manifestazione,((Utente) upat.getPrincipal())));
	}
	@Operation(summary = "Metodo per controllare le statistiche dei biglietti di un evento", description = "Questo EndPoint ci restituisce le statistiche dei biglietti di un evento appartenente ad una manifestazione organizzata dall'utente venditore che effettua la richiesta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Viene restituita una response con dentro la manifestazione, il singolo evento e il luogo in cui e' svolto ed i suoi settori con le varie vendite dei biglietti effettuati per i singoli settori", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StatisticheManifestazioneDTOResponse.class))}),
			@ApiResponse(responseCode = "403", description = "L'utente che ha mandato la richiesta non ha i permessi", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "403", description = "L'utente che ha mandato la richiesta non e' l'organizzatore della manifestazione", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "L'evento non e' stato trovato tramite l'id inserito in input", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "La manifestazione non e' stata trovata tramite l'id presente nell'evento", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "Il luogo non e' stato trovato tramite l'id presente nell'evento", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "I settori non sono stati trovati tramite l'id del luogo", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "I prezzo settore eventi non sono stati trovati tramite l'id dell'evento", content = {@Content(mediaType = MediaType.ALL_VALUE)})
	})
	@PostMapping("/evento/stats/biglietti/{id}")
	public ResponseEntity<DatiEventiDTOResponse> statisticheBigliettiPerEvento(@PathVariable("id") long id_evento, UsernamePasswordAuthenticationToken upat) {
		return ResponseEntity.ok(vendFac.statisticheBigliettiPerEvento(id_evento, ((Utente)upat.getPrincipal())));
	}
	@Operation(summary = "Metodo per il setup di un prezzo settore evento", description = "Questo EndPoint permette all'utente venditore di impostare un prezzo settore evento")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Viene restituito il prezzo settore evento modificato", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = StatisticheManifestazioneDTOResponse.class))}),
			@ApiResponse(responseCode = "403", description = "L'utente che ha mandato la richiesta non ha i permessi", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "403", description = "L'utente che ha mandato la richiesta non e' l'organizzatore della manifestazione", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "L'evento non e' stato trovato tramite l'id evento inserito in input", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "La manifestazione non e' stata trovata tramite l'id presente nell'evento", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "Il settore non e' stato trovato tramite l'id settore inserito in input", content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404", description = "Il prezzo settore evento non e' stato trovato tramite l'id prezzo-settore-evento inserito in input", content = {@Content(mediaType = MediaType.ALL_VALUE)})
	})
	@PostMapping("/set/prezzo-settore-evento")
	public ResponseEntity<PseDTOResponse> setPrezzoSettoreEvento(@RequestBody @Valid PrezzoSettoreEventoDTORequest request, UsernamePasswordAuthenticationToken upat) {
		return ResponseEntity.ok(vendFac.setPrezzoSettoreEvento(request, ((Utente)upat.getPrincipal())));
	}

	 

}
