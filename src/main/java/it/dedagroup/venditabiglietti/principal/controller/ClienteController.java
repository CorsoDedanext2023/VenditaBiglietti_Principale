package it.dedagroup.venditabiglietti.principal.controller;

import it.dedagroup.venditabiglietti.principal.dto.request.ModificaUtenteLoggatoRequest;
import it.dedagroup.venditabiglietti.principal.dto.response.BigliettoMicroDTO;
import it.dedagroup.venditabiglietti.principal.facade.ClienteFacade;
import it.dedagroup.venditabiglietti.principal.model.Utente;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/cliente")
@Tag (name="Endpoint microservizio Settore",
description = "Questo controller gestisce il microservizio del Settore e interagisce sul database solo sulla tabella Settore")
@AllArgsConstructor
@Validated
public class ClienteController {


    private final ClienteFacade clienteFacade;

@Operation(summary = "Endpoint che permette di visualizzare la cronologia acquisti di un utente",
					description = "Questo metodo serve a restituire una lista di biglietti secondo l'id dell'utente passato come parametro.Se la lista viene trovata nel db (anche se vuota), risponderà con codice 200."+
										"Se l'idUtente passato come parametro è minore o uguale a 0, oppure è un dato non valido, il metodo risponderà con codice 400")
    @GetMapping("/{id}/cronologia-acquisti")
@ApiResponses({
	@ApiResponse(responseCode = "SUCCESS(200)",description = "La lista dei biglietti è stata trovata",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = BigliettoMicroDTO.class))),
	@ApiResponse(responseCode = "BADREQUEST(400)",description = "Impossibile trovare la cronologia di biglietti, non esiste nessun utente con questo id",content = @Content(mediaType = MediaType.ALL_VALUE))
})
    public ResponseEntity<List<BigliettoMicroDTO>> cronologiaBiglietti(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(clienteFacade.cronologiaBigliettiAcquistati(id));
    }


@Operation(summary = "Endpoint che disattiva un utente",description = "Questo metodo serve a disattivare un utente. Passando un un utente leggiamo il suo id, controlliamo se esite nel db e settiamo la variabile isCancellato a true. Se l'utente non esiste viene lanciata un eccezione e il metodo risponderà con codice 404, altrimenti rispondiamo 202")
@ApiResponses({
	@ApiResponse(responseCode ="ACCEPTED(202)" ,description = "Cliente disattivato correttamente",content = @Content(mediaType = MediaType.ALL_VALUE)),
	@ApiResponse(responseCode = "NOTFOUND(404)",description = "Impossibile disattivare questo account: non esiste nessun cliente con questo id",content = @Content(mediaType = MediaType.ALL_VALUE)) 							
})
    @PutMapping("/disattiva/{id}")
    public ResponseEntity<String> disattivaUtente(@PathVariable Long id){
        clienteFacade.disattivaUtente(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Hai disattivato l'account");
    }


    @Operation(summary = "Endpoint che permette ad un cliente di modificare i suoi dati",
    		description = "In questo metodo, passando per una request in cui saranno contenuti: i vecchi dati,utili ad autenticare l'utente e verificare che realmente esista; i nuovi dati che potranno essere password, email o numero di telefono"
    							+"risponderà 200 se la odifica viene fatta correttamente, altrimenti 404 se l'utente inserisce dati non validi ")
    @ApiResponses({
    	@ApiResponse(responseCode ="ACCEPTED(202)" ,description = "Cliente disattivato correttamente",content = @Content(mediaType = MediaType.ALL_VALUE)
    				),
    	@ApiResponse(responseCode = "NOTFOUND(404)",
    							description = "Impossibile disattivare questo account: non esiste nessun cliente con questo id",
    							content = @Content(mediaType = MediaType.ALL_VALUE))
    })
    @PutMapping("/modificaDati")
    public ResponseEntity<String> modificaUtente(@Valid @RequestBody ModificaUtenteLoggatoRequest request){
        clienteFacade.modificaUtente(request);
        return ResponseEntity.status(HttpStatus.OK).body("La modifica dei dati è avvenuta con successo");
    }
    
    @Operation(summary = "Endpoint che permette ad un cliente di acquistare un biglietto",
    					description = "Questo metodo serve ad acquistare un biglietto. Passando un id che rappresenta il prezzo del biglietto di un evento in un determinato settore i un controlliamo che sia valido,e passando il token dell'utente controlliamo che sia autenticato e che abbia i permessi."+
    										  "Se l'utente non ha i permessi risponderemo con codice 401, se l'id passato non è valido risponderemo 400, se non esiste un biglietto per quell'evento in quel settore risponderemo 404, altrimenti se tutto va a buon fine risponderemo 200")
    @ApiResponses({
    	@ApiResponse(responseCode = "SUCCESS(200)",description = "il biglietto è stato acquistato",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema =@Schema(implementation = BigliettoMicroDTO.class))),
    	@ApiResponse(responseCode = "UNAUTHORIZED(401)",description = "Impossibile acquistare biglietto, l'utente che vuole acquistare il biglietto non ha i permessi per farlo",content = @Content(mediaType = MediaType.ALL_VALUE)),
    	@ApiResponse(responseCode="BADREQUEST(400)",description = "Impossibile acquistare biglietto, l'id passato come parametro al metodo non è valido",content = @Content(mediaType = MediaType.ALL_VALUE)),
    	@ApiResponse(responseCode = "NOTFOUND(404)",description = "Impossibile acquistare biglietto, questo evento non esiste o questo settore non è disponibile",content = @Content(mediaType = MediaType.ALL_VALUE))
    })
    @PostMapping("/acquista-biglietto/{idPrezzoSettoreEvento}")
    public ResponseEntity<BigliettoMicroDTO> acquistaBiglietto(@PathVariable Long idPrezzoSettoreEvento, UsernamePasswordAuthenticationToken token) {
        Utente principal = (Utente)token.getPrincipal();
        long idPrincipal = principal.getId();
      return ResponseEntity.status(HttpStatus.OK).body(clienteFacade.acquistaBiglietto(idPrezzoSettoreEvento, idPrincipal));
    }
}
