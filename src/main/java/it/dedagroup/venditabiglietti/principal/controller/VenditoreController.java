package it.dedagroup.venditabiglietti.principal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.dedagroup.venditabiglietti.principal.dto.request.AddEventoDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.AddManifestazioneDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.ModifyPSEDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.AddEventoResponse;
import it.dedagroup.venditabiglietti.principal.dto.response.DatiEventiDTOResponse;
import it.dedagroup.venditabiglietti.principal.dto.response.LuogoMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.response.ManifestazioneDTOResponse;
import it.dedagroup.venditabiglietti.principal.dto.response.PseDTOResponse;
import it.dedagroup.venditabiglietti.principal.dto.response.StatisticheManifestazioneDTOResponse;
import it.dedagroup.venditabiglietti.principal.dto.response.VisualizzaEventoManifestazioneDTOResponse;
import it.dedagroup.venditabiglietti.principal.facade.VenditoreFacade;
import it.dedagroup.venditabiglietti.principal.model.Utente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

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
			@ApiResponse(responseCode = "404", description = "La manifestazione inserita e' gia' esistente", content = @Content(mediaType = MediaType.ALL_VALUE)),
			@ApiResponse(responseCode = "400", description = "La categoria non e' stata trovata con l'id inserito", content = @Content(mediaType = MediaType.ALL_VALUE))
	})
	@PostMapping("/manifestazione/add")
	public ResponseEntity<String> addManifestazione(@RequestBody @Valid AddManifestazioneDTORequest request, UsernamePasswordAuthenticationToken upat){
		return ResponseEntity.ok(vendFac.addManifestazione(request, ((Utente)upat.getPrincipal())));
	}
	@Operation(summary = "Endpoint che permette di aggiungere un evento solo se l'utente è un venditore e se la manifestazione e luogo sono già presenti",
				description = "questo metodo prende in input un evento e lo aggiunge nel DB, solo dopo il verificamento del ruolo Utente(venditore) tramite upat e dopo aver verificato che "
						+ "Manifestazione e Luogo siano già esistenti")
	@ApiResponses({
		@ApiResponse(responseCode = "ACCEPTED(202)",
				description = "Evento aggiunto correttamente",
				content = @Content(mediaType = MediaType.ALL_VALUE)),
		@ApiResponse(responseCode = "403",
				description = "L'utente che ha mandato la richiesta non hai permessi",
				content = {@Content(mediaType = MediaType.ALL_VALUE)}),
		@ApiResponse(responseCode = "403",
				description = "L'utente che ha mandato la richiesta non e' l'organizzatore della manifestazione",
				content = {@Content(mediaType = MediaType.ALL_VALUE)}),
		@ApiResponse(responseCode = "NOTFOUND(404)",
				description = "Impossibile aggiungere Evento",
				content = @Content(mediaType = MediaType.ALL_VALUE))

	})
	@PostMapping("/evento/add")
	public ResponseEntity<AddEventoResponse> addEvento(@RequestBody @Valid AddEventoDTORequest eventoRequest, UsernamePasswordAuthenticationToken upat){
		return ResponseEntity.ok(vendFac.addEvento(eventoRequest, ((Utente)upat.getPrincipal())));
	}
	
	@Operation(summary = "Endpoint che permette di modificare un evento solo se l'utente è un venditore e se esiste un idManifestazione",
			description = "Questo metodo ci permette di settare la booleana(isCancellato) a true, solo dopo il verificamento del ruolo Utente(venditore) tramite upat e che l'idManifestazione inserito sia già presente")
	@ApiResponses({
		@ApiResponse(responseCode = "ACCEPTED(202)",
				description = "Evento modificato correttamente",
				content = @Content(mediaType = MediaType.ALL_VALUE)),
		@ApiResponse(responseCode = "403",
				description = "L'utente che ha mandato la richiesta non hai permessi",
				content = {@Content(mediaType = MediaType.ALL_VALUE)}),
		@ApiResponse(responseCode = "403",
				description = "L'utente che ha mandato la richiesta non e' l'organizzatore della manifestazione",
				content = {@Content(mediaType = MediaType.ALL_VALUE)}),
		@ApiResponse(responseCode = "NOTFOUND(404)",
				description = "Impossibile modificare Evento",
				content = @Content(mediaType = MediaType.ALL_VALUE))
	})
	@PostMapping("/evento/delete/{id}")
	public ResponseEntity<Void> deleteEvento(@Valid @Positive(message = "Inserire un id valido positivo")
												 @Min(value = 1, message = "Inserire un id maggiore o uguale a 1: ID Manifestazione")
												 @PathVariable("id") long idManifestazione, UsernamePasswordAuthenticationToken upat){
		vendFac.deleteEvento(idManifestazione, ((Utente)upat.getPrincipal()));
		return ResponseEntity.ok().build();
	}
	@Operation(summary = "Endpoint che permette di visualizzare un evento solo se l'utente è un venditore e se esiste un idManifestazione",
			description = "Questo metodo ci permette di visualizzare una manifestazione(che puo avere diversi eventi) tramite idManifestazione e solo dopo aver verificato che l'Utente sia un Venditore")
	@ApiResponses({
		@ApiResponse(responseCode = "ACCEPTED(202)",
				description = "Manifestazione Trovata",
				content = @Content(mediaType = MediaType.ALL_VALUE)),
		@ApiResponse(responseCode = "403",
				description = "L'utente che ha mandato la richiesta non hai permessi",
				content = {@Content(mediaType = MediaType.ALL_VALUE)}),
		@ApiResponse(responseCode = "403",
				description = "L'utente che ha mandato la richiesta non e' l'organizzatore della manifestazione",
				content = {@Content(mediaType = MediaType.ALL_VALUE)}),
		@ApiResponse(responseCode = "NOTFOUND(404)",
				description = "Impossibile trovare Manifestazione",
				content = @Content(mediaType = MediaType.ALL_VALUE))

	})
	@PostMapping("/evento/visualizza/{idManifestazione}")
	public ResponseEntity<VisualizzaEventoManifestazioneDTOResponse> visualizzaEventiOrganizzati(@Valid @Positive(message = "Inserire un id valido positivo")
																									 @Min(value = 1, message = "Inserire un id maggiore o uguale a 1: ID Manifestazione")
																									 @PathVariable long idManifestazione, UsernamePasswordAuthenticationToken upat){
		return ResponseEntity.ok(vendFac.visualizzaEventiOrganizzati(idManifestazione, ((Utente)upat.getPrincipal())));
	}
	@Operation(summary = "Metodo per controllare le statistiche dei biglietti di una manifestazione", description = "Questo EndPoint ci restituisce le statistiche dei biglietti di una manifestazione appartenente all'utente venditore che effettua la richiesta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Viene restituita una response con dentro la manifestazione i vari eventi nei rispettivi luoghi con le varie vendite dei biglietti effettuati per i singoli settori",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = StatisticheManifestazioneDTOResponse.class))}),
			@ApiResponse(responseCode = "403",
					description = "L'utente che ha mandato la richiesta non ha i permessi",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "403",
					description = "L'utente che ha mandato la richiesta non e' l'organizzatore della manifestazione",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "La manifestazione non e' stata trovata con l'id inserito dall'utente venditore",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "Gli eventi non sono stati trovati con l'id della manifestazione inserito",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "I luoghi non sono stati trovati con i vari id luogo presenti negli eventi inseriti",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "I settori non sono stati trovati con i vari id luogo presenti negli eventi inseriti",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "I prezzo settore eventi non sono stati trovati con i vari id degli eventi inseriti",
					content = {@Content(mediaType = MediaType.ALL_VALUE)})
	})
	@PostMapping("/manifestazione/stats/biglietti/{id}")
	public ResponseEntity<StatisticheManifestazioneDTOResponse> statisticheBigliettiPerManifestazione(@Valid @Positive(message = "Inserire un id valido positivo")
																										  @Min(value = 1, message = "Inserire un id maggiore o uguale a 1: ID Manifestazione")
																										  @PathVariable("id") long id_manifestazione,UsernamePasswordAuthenticationToken upat){
		return ResponseEntity.ok(vendFac.statisticheBigliettiPerManifestazione(id_manifestazione,((Utente) upat.getPrincipal())));
	}
	@Operation(summary = "Metodo per controllare le statistiche dei biglietti di un evento", description = "Questo EndPoint ci restituisce le statistiche dei biglietti di un evento appartenente ad una manifestazione organizzata dall'utente venditore che effettua la richiesta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Viene restituita una response con dentro la manifestazione, il singolo evento e il luogo in cui e' svolto ed i suoi settori con le varie vendite dei biglietti effettuati per i singoli settori",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = StatisticheManifestazioneDTOResponse.class))}),
			@ApiResponse(responseCode = "403",
					description = "L'utente che ha mandato la richiesta non ha i permessi",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "403",
					description = "L'utente che ha mandato la richiesta non e' l'organizzatore della manifestazione",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "L'evento non e' stato trovato tramite l'id inserito in input",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "La manifestazione non e' stata trovata tramite l'id presente nell'evento",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "Il luogo non e' stato trovato tramite l'id presente nell'evento",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "I settori non sono stati trovati tramite l'id del luogo",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "I prezzo settore eventi non sono stati trovati tramite l'id dell'evento",
					content = {@Content(mediaType = MediaType.ALL_VALUE)})
	})
	@PostMapping("/evento/stats/biglietti/{id}")
	public ResponseEntity<DatiEventiDTOResponse> statisticheBigliettiPerEvento(@Valid @Positive(message = "Inserire un id valido positivo")
																				   @Min(value = 1, message = "Inserire un id maggiore o uguale a 1: ID Evento")
																				   @PathVariable("id") long id_evento, UsernamePasswordAuthenticationToken upat) {
		return ResponseEntity.ok(vendFac.statisticheBigliettiPerEvento(id_evento, ((Utente)upat.getPrincipal())));
	}
	@Operation(summary = "Metodo per il setup di un prezzo settore evento", description = "Questo EndPoint permette all'utente venditore di impostare un prezzo settore evento")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Viene restituito il prezzo settore evento modificato",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = StatisticheManifestazioneDTOResponse.class))}),
			@ApiResponse(responseCode = "403",
					description = "L'utente che ha mandato la richiesta non ha i permessi",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "403",
					description = "L'utente che ha mandato la richiesta non e' l'organizzatore della manifestazione",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "L'evento non e' stato trovato tramite l'id evento inserito in input",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "La manifestazione non e' stata trovata tramite l'id presente nell'evento",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "Il settore non e' stato trovato tramite l'id settore inserito in input",
					content = {@Content(mediaType = MediaType.ALL_VALUE)}),
			@ApiResponse(responseCode = "404",
					description = "Il prezzo settore evento non e' stato trovato tramite l'id prezzo-settore-evento inserito in input",
					content = {@Content(mediaType = MediaType.ALL_VALUE)})
	})
	@PostMapping("/set/prezzo-settore-evento")
	public ResponseEntity<PseDTOResponse> setPrezzoSettoreEvento(@RequestBody @Valid ModifyPSEDTORequest request, UsernamePasswordAuthenticationToken upat) {
		return ResponseEntity.ok(vendFac.setPrezzoSettoreEvento(request, ((Utente)upat.getPrincipal())));
	}


	@Operation(summary = "metodo per cerlare una lista di luoghi inserendo per ogni attributo una chiave valore(String) e un valore(String)",
			description = "in questo metodo inserendo attributo di luogo (utilizzando un Map) una chiave ed un valore entrambe String, ci ritornerà una Lista di Luoghi da noi selezionati, solo se l'utente sia un venditore")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Lista Luoghi trovata tramite attributi ineriti",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = LuogoMicroDTO[].class))),
			@ApiResponse(responseCode = "403",
					description = "L'utente non è un venditore",
					content = @Content(mediaType = MediaType.ALL_VALUE)),
			@ApiResponse(responseCode = "400",
					description = "La lista degli attributi di luogo e' vuota",
					content = @Content(mediaType = MediaType.ALL_VALUE))
	})
	@PostMapping("/filtra/luoghi/map")
	public ResponseEntity<List<LuogoMicroDTO>> filtraLuoghiMap(@RequestBody @Valid @NotEmpty(message = "Valorizzare la map degli attributi di luogo") Map<String, String> map, UsernamePasswordAuthenticationToken upat){
		return ResponseEntity.ok(vendFac.filtraLuoghiMap(map, (Utente)upat.getPrincipal()));
	}
	
}
