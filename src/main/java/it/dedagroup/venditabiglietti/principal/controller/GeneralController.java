package it.dedagroup.venditabiglietti.principal.controller;

import static it.dedagroup.venditabiglietti.principal.util.UtilPath.EVENTI_FUTURI_CON_BIGLIETTI;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.LOGIN;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.REGISTRAZIONE;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.dedagroup.venditabiglietti.principal.dto.response.MostraEventiFuturiDTOResponse;
import it.dedagroup.venditabiglietti.principal.dto.request.EventiFiltratiDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.EventiFiltratiDTOResponse;
import it.dedagroup.venditabiglietti.principal.exception.response.BadRequestDTOResponse;
import it.dedagroup.venditabiglietti.principal.exception.response.ErrorMessageDTOResponse;
import it.dedagroup.venditabiglietti.principal.exception.response.ErrorMessageListDTOResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import it.dedagroup.venditabiglietti.principal.dto.request.AggiungiUtenteDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.LoginDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.EventoDTOResponse;
import it.dedagroup.venditabiglietti.principal.facade.GeneralFacade;
import jakarta.validation.Valid;

import java.util.List;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.*;

@RestController
@RequestMapping(GENERAL_PATH)
public class GeneralController {

    @Autowired
    GeneralFacade gFac;

    @Operation(summary = "Registrazione utente semplice.", description = "Questo endpoint permette a un utente non loggato di registrarsi come utente semplice.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "L'utente è riuscito a registrarsi correttamente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Email immessa già esistente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "Numero di telefono immesso già esistente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "Uno o più campi necessari vuoti.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "L'email inserita non è nel formato corretto.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "La password inserita contiene meno di otto caratteri.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "La password inserita non contiene almeno un simbolo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "La password inserita non contiene almeno un numero.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "La password inserita non contiene almeno una lettera maiuscola.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "La password inserita non contiene almeno una lettera minuscola.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class)))
    })
    @PostMapping(REGISTRAZIONE)
    public ResponseEntity<Void> registrazioneCliente(@Valid @RequestBody AggiungiUtenteDTORequest req){
        gFac.registrazioneCliente(req);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping(EVENTI_FUTURI_CON_BIGLIETTI)
    public ResponseEntity<List<MostraEventiFuturiDTOResponse>> eventiFutConBiglietti(){
        return ResponseEntity.status(HttpStatus.OK).body(gFac.trovaEventiFuturiConBiglietti());
    }

    @Operation(summary = "Filtro degli eventi, con ancora biglietti, per vari campi.", description = "Questo endpoint permette a un utente non loggato di filtrare gli eventi, " +
            "a partire da quelli con ancora biglietti, a partire da provincia, comune, descrizione, data immessa (mostrando solo eventi successivi a essa), nome della manifestazione e della categoria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sono stati trovati risultati coerenti con i criteri specificicati.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Nessun evento trovato con questi criteri.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BadRequestDTOResponse.class)))
    })
    @PostMapping(FILTRA_EVENTI_CON_BIGLIETTI)
    public ResponseEntity<List<EventiFiltratiDTOResponse>> filtraEventiConBiglietti(@RequestBody EventiFiltratiDTORequest request){
        return ResponseEntity.status(HttpStatus.OK).body(gFac.eventiFiltrati(request));
    }

    @Operation(summary = "Login utente.", description = "Questo endpoint permette a un utente non loggato di effettuare l'accesso con email e password. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "L'utente è riuscito a effettuare il login correttamente.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "Campo email o password vuoto.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "L'email inserita non è nel formato corretto.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "La password inserita contiene meno di otto caratteri.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "La password inserita non contiene almeno un simbolo.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "La password inserita non contiene almeno un numero.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "La password inserita non contiene almeno una lettera maiuscola.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class))),
            @ApiResponse(responseCode = "400", description = "La password inserita non contiene almeno una lettera minuscola.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessageListDTOResponse.class)))
    })
    @PostMapping(LOGIN)
    public ResponseEntity<Void> login (@Valid @RequestBody LoginDTORequest request){
        return ResponseEntity.status(HttpStatus.ACCEPTED).header("Authorization",gFac.login(request)).build();
    }

}
