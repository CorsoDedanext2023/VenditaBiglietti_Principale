package it.dedagroup.venditabiglietti.principal.facade;

import it.dedagroup.venditabiglietti.principal.dto.request.AddBigliettoDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.ModificaUtenteLoggatoRequest;
import it.dedagroup.venditabiglietti.principal.dto.response.BigliettoMicroDTO;
import it.dedagroup.venditabiglietti.principal.model.Biglietto;
import it.dedagroup.venditabiglietti.principal.model.PrezzoSettoreEvento;
import it.dedagroup.venditabiglietti.principal.model.Utente;
import it.dedagroup.venditabiglietti.principal.service.BigliettoServiceDef;
import it.dedagroup.venditabiglietti.principal.service.PrezzoSettoreEventoServiceDef;
import it.dedagroup.venditabiglietti.principal.service.UtenteServiceDef;
import it.dedagroup.venditabiglietti.principal.serviceimpl.UtenteServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ClienteFacade {
    @Autowired
    private UtenteServiceImpl service;

    private final BigliettoServiceDef bigliettoService;
    private final PrezzoSettoreEventoServiceDef settoreEventoService;


    public void disattivaUtente(Long id){
        service.eliminaUtente(id);
    }

    public List<BigliettoMicroDTO> cronologiaBigliettiAcquistati(Long id){
        service.findById(id);
        return bigliettoService.findAllByIdUtente(id);
    }
    public void modificaUtente(ModificaUtenteLoggatoRequest request) {
    	Utente utenteDaMod=service.findByEmailAndPassword(request.getEmailAttuale(), request.getPasswordAttuale());
    	 String nuovaEmail = request.getNuovaEmail();
         String nuovaPassword = request.getNuovaPassword();
         String nuovoTelefono = request.getNuovoTelefono();
         if (nuovaEmail != null && !nuovaEmail.isEmpty()) {
        	 utenteDaMod.setEmail(nuovaEmail);
         }
         if (nuovaPassword != null && !nuovaPassword.isEmpty()) {
        	 utenteDaMod.setPassword(nuovaPassword);
         }
         if (nuovoTelefono != null && !nuovoTelefono.isEmpty()) {
        	 utenteDaMod.setTelefono(nuovoTelefono);
         }
    	service.modificaUtente(utenteDaMod, utenteDaMod.getId());
    }

    public BigliettoMicroDTO acquistaBiglietto(Long idPrezzoSettoreEvento, long idUtente) {
        if (idPrezzoSettoreEvento > 0 ){
            PrezzoSettoreEvento newPrezzoSettoreEvento = settoreEventoService.findById(idPrezzoSettoreEvento);
            Biglietto newBiglietto = new Biglietto();
            newBiglietto.setDataAcquisto(LocalDate.now());
            newBiglietto.setPrezzo(newPrezzoSettoreEvento.getPrezzo());
            newBiglietto.setUtente(service.findById(idUtente));
            AddBigliettoDTORequest request=new AddBigliettoDTORequest();
            request.setIdUtente(idUtente);
            request.setIdPrezzoSettore(idPrezzoSettoreEvento);
            return bigliettoService.saveBiglietto(request);
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'id deve essere maggiore di zero.");
    }
}
