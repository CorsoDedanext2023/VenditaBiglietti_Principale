package it.dedagroup.venditabiglietti.principal.controller;

import static it.dedagroup.venditabiglietti.principal.util.UtilPath.EVENTI_FUTURI_CON_BIGLIETTI;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.LOGIN;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.REGISTRAZIONE;

import java.util.List;

import it.dedagroup.venditabiglietti.principal.dto.response.MostraEventiFuturiDTOResponse;
import it.dedagroup.venditabiglietti.principal.dto.request.EventiFiltratiDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.EventiFiltratiDTOResponse;
import it.dedagroup.venditabiglietti.principal.model.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.dedagroup.venditabiglietti.principal.dto.request.AggiungiUtenteDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.LoginDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.EventoDTOResponse;
import it.dedagroup.venditabiglietti.principal.facade.GeneralFacade;
import it.dedagroup.venditabiglietti.principal.model.Luogo;
import jakarta.validation.Valid;

import java.util.List;
import static it.dedagroup.venditabiglietti.principal.util.UtilPath.*;

import java.util.List;

import static it.dedagroup.venditabiglietti.principal.util.UtilPath.*;

@RestController
@RequestMapping(GENERAL_PATH)
public class GeneralController {

    @Autowired
    GeneralFacade gFac;

    @PostMapping(REGISTRAZIONE)
    public ResponseEntity<Void> registrazioneCliente(@Valid @RequestBody AggiungiUtenteDTORequest req){
        gFac.registrazioneCliente(req);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping(EVENTI_FUTURI_CON_BIGLIETTI)
    public ResponseEntity<List<MostraEventiFuturiDTOResponse>> eventiFutConBiglietti(){
        return ResponseEntity.status(HttpStatus.OK).body(gFac.trovaEventiFuturiConBiglietti());
    }


    @PostMapping(LOGIN)
    public ResponseEntity<Void> login (@RequestBody LoginDTORequest request){
        return ResponseEntity.status(HttpStatus.ACCEPTED).header("Authorization",gFac.login(request)).build();
    }

    @GetMapping("/testCriteria")
    public ResponseEntity<List<EventiFiltratiDTOResponse>> criteria(@RequestBody EventiFiltratiDTORequest request){
        return ResponseEntity.accepted().body(gFac.eventiFiltrati(request));
    }
}
