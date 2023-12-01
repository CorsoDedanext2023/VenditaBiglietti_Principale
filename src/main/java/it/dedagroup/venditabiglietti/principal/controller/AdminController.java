package it.dedagroup.venditabiglietti.principal.controller;

import static it.dedagroup.venditabiglietti.principal.util.UtilPath.ADMIN_PATH;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.AGGIUNGI_CATEGORIA;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.AGGIUNGI_LUOGO;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.AGGIUNGI_SETTORE;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.AGGIUNGI_UTENTE_VENDITORE;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.ELIMINA_EVENTO;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.ELIMINA_LUOGO;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.ELIMINA_MANIFESTAZIONE;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.ELIMINA_UTENTE_CLIENTE;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.ELIMINA_UTENTE_VENDITORE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.dedagroup.venditabiglietti.principal.dto.request.AggiungiCategoriaDtoRequest;
import it.dedagroup.venditabiglietti.principal.dto.request.AggiungiSettoreDtoRequest;
import it.dedagroup.venditabiglietti.principal.dto.request.AggiungiUtenteDTORequest;
import it.dedagroup.venditabiglietti.principal.exception.response.ErrorMessageDTOResponse;
import it.dedagroup.venditabiglietti.principal.facade.AdminFacade;
import it.dedagroup.venditabiglietti.principal.model.Luogo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping(ADMIN_PATH)
@Validated
@Tag(name = "Controller che gestisce l'utenza ADMIN", description = "Tutti i metodi che sono implementati in questo controller gestiscono l'utenza admin e come interagisce sul sito")
public class AdminController {
	

	@Autowired
	AdminFacade facade;

	@Operation(summary = "Disattiva un utente con ruolo VENDITORE", description = "Questo endpoint consente di disattivare un utente con il ruolo di VENDITORE nel sistema. "
			+ "L'utente viene disattivato e non sarà più in grado di eseguire operazioni. "
			+ "È necessario fornire l'ID dell'utente come parametro nella richiesta.\n\n"
			+ "Nota: La disattivazione di un utente comporta l'invocazione del microservizio 'VenditoreBiglietti_Security' "
			+ "per garantire la coerenza dei dati nel sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "L'utente è stato disattivato, nel database sarà settato con is_cancellato a true"),
			@ApiResponse(responseCode = "400", description = "La richiesta de client non è valida, il suo dato input non è corretto", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTOResponse.class))) })
	@PostMapping(ELIMINA_UTENTE_VENDITORE)
	public ResponseEntity<Void> eliminaVenditore(
			@RequestParam @Min(value = 1, message = "id non valido per eliminare l'utente venditore") long id) {
		facade.eliminaVenditore(id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@Operation(summary = "Disattiva un utente con ruolo Cliente", description = "Questo endpoint consente di disattivare un utente con il ruolo di cliente nel sistema. "
			+ "L'utente viene disattivato e non sarà più in grado di eseguire operazioni. "
			+ "È necessario fornire l'ID dell'utente come parametro nella richiesta.\n\n"
			+ "Nota: La disattivazione di un utente comporta l'invocazione del microservizio 'VenditoreBiglietti_Security' "
			+ "per garantire la coerenza dei dati nel sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "L'utente è stato disattivato, nel database sarà settato con is_cancellato a true"),
			@ApiResponse(responseCode = "400", description = "La richiesta de client non è valida, il suo dato input non è corretto", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTOResponse.class))) })
	@PostMapping(ELIMINA_UTENTE_CLIENTE)
	public ResponseEntity<Void> eliminaCliente(
			@RequestParam @Min(value = 1, message = "id non valido per eliminare l'utente cliente") long id) {
		facade.eliminaCliente(id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@Operation(summary = "Aggiungi un nuovo utente con ruolo VENDITORE", description = "Questo endpoint consente di aggiungere un nuovo utente con il ruolo di VENDITORE al sistema. "
			+ "I dettagli dell'utente devono essere forniti nel corpo della richiesta usando il formato JSON. "
			+ "Gli attributi richiesti includono almeno nome, cognome, indirizzo email, password, data di nascita e telefono. "
			+ "Dopo l'aggiunta, l'utente sarà in grado di accedere al sistema con i privilegi di un venditore.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "L'utente è stato aggiunto con successo"),
			@ApiResponse(responseCode = "400", description = "La richiesta del client non è valida. Assicurarsi che i dati di input siano corretti.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTOResponse.class))) })
	@PostMapping(AGGIUNGI_UTENTE_VENDITORE)
	public ResponseEntity<Void> aggiungiVenditore(@Valid @RequestBody AggiungiUtenteDTORequest request) {
		facade.aggiungiVenditore(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "Aggiungi una nuova categoria", description = "Questo endpoint consente di aggiungere una nuova categoria al sistema. "
			+ "I dettagli della categoria devono essere forniti nel corpo della richiesta usando il formato JSON. "
			+ "L'attributo richiesto è il nome della categoria, che deve essere una frase di lettere.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "La categoria è stata aggiunta con successo"),
			@ApiResponse(responseCode = "400", description = "La richiesta del client non è valida. Assicurarsi che i dati di input siano corretti.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTOResponse.class))) })
	@PostMapping(AGGIUNGI_CATEGORIA)
	public ResponseEntity<Void> aggiungiCategoria(@Valid @RequestBody AggiungiCategoriaDtoRequest request) {
		facade.aggiungiCategoria(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();

	}

	@Operation(summary = "Aggiungi un nuovo settore", description = "Questo endpoint consente di aggiungere un nuovo settore al sistema. "
			+ "I dettagli del settore devono essere forniti nel corpo della richiesta usando il formato JSON. "
			+ "Gli attributi richiesti includono il nome del settore, il numero di posti e l'ID del luogo associato.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "Il settore è stato aggiunto con successo"),
			@ApiResponse(responseCode = "400", description = "La richiesta del client non è valida. Assicurarsi che i dati di input siano corretti.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTOResponse.class))) })
	@PostMapping(AGGIUNGI_SETTORE)
	public ResponseEntity<Void> aggiungiSettore(@Valid @RequestBody AggiungiSettoreDtoRequest request) {
		facade.aggiungiSettore(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@Operation(summary = "Elimina una manifestazione", description = "Questo endpoint consente di eliminare una manifestazione dal sistema. "
			+ "L'ID della manifestazione da eliminare deve essere fornito come parte dell'URL della richiesta.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "La manifestazione è stata eliminata con successo"),
			@ApiResponse(responseCode = "400", description = "La richiesta del client non è valida. Assicurarsi che l'ID della manifestazione sia corretto.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTOResponse.class))) })
	@PostMapping(ELIMINA_MANIFESTAZIONE + "/{id}")
	public ResponseEntity<Void> eliminaManifestazione(@PathVariable long id) {
		facade.eliminaManifestazione(id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@Operation(summary = "Elimina un evento", description = "Questo endpoint consente di eliminare una evento dal sistema. "
			+ "L'ID dell'evento da eliminare deve essere fornito come parte dell'URL della richiesta.")
	@ApiResponses(value = { @ApiResponse(responseCode = "202", description = "L'evento è stato eliminato con successo"),
			@ApiResponse(responseCode = "400", description = "La richiesta del client non è valida. Assicurarsi che l'ID dell'evento sia corretto.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTOResponse.class))) })
	@PostMapping(ELIMINA_EVENTO + "/{id}")
	public ResponseEntity<Void> eliminaEvento(@PathVariable long id) {
		facade.eliminaEvento(id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@Operation(summary = "Elimina un luogo", description = "Questo endpoint consente di eliminare un luogo dal sistema. "
			+ "L'ID del luogo da eliminare deve essere fornito come parte dell'URL della richiesta.")
	@ApiResponses(value = { @ApiResponse(responseCode = "202", description = "Il luogo è stato eliminato con successo"),
			@ApiResponse(responseCode = "400", description = "La richiesta del client non è valida. Assicurarsi che l'ID del luogo sia corretto.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageDTOResponse.class))) })
	@PostMapping(ELIMINA_LUOGO + "/{id}")
	public ResponseEntity<Void> eliminaLuogo(@PathVariable long id) {
		facade.eliminaLuogo(id);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PostMapping(AGGIUNGI_LUOGO)
	public ResponseEntity<Void> aggiungiLuogo(@RequestBody Luogo request) {
		facade.aggiungiLuogo(request);;
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

}
