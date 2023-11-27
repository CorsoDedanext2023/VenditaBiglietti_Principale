package it.dedagroup.venditabiglietti.principal.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import it.dedagroup.venditabiglietti.principal.dto.request.LoginDTORequest;
import it.dedagroup.venditabiglietti.principal.service.GeneralCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.dedagroup.venditabiglietti.principal.model.Ruolo;
import it.dedagroup.venditabiglietti.principal.model.Utente;
import it.dedagroup.venditabiglietti.principal.repository.UtenteRepository;
import it.dedagroup.venditabiglietti.principal.service.UtenteServiceDef;
import jakarta.transaction.Transactional;

@Service

public class UtenteServiceImpl implements UtenteServiceDef, GeneralCallService {

	private final String pathUtente="http://localhost:8092/utente";

	//TODO pulire i metodi togliendo le duplicazioni



	@Override
	public Utente findByEmailAndPassword(String email, String password) {
		return callPost(pathUtente + "/trovaPerEmailEPassword", null, Utente.class);
	}

	@Override
	public Utente findByTelefono(String telefono) {
		return callPost(pathUtente + "/trovaPerTelefono", null, Utente.class);
	}

	@Override
	public Utente findByData_Di_Nascita(LocalDate data_di_nascita) {
		return callPost(pathUtente + "/trovaPerDataDiNascita", null, Utente.class);
	}

	@Override
	public Utente findByNomeAndCognome(String nome, String cognome) {
		return callPost(pathUtente + "/trovaPerNomeECognome", null, Utente.class);
	}

	@Override
	public Utente findByRuolo(Ruolo ruolo) {
		return callPost(pathUtente + "/trovaPerRuolo", null, Utente.class);
	}

	@Override
	@Transactional(rollbackOn = DataAccessException.class)
	public void aggiungiUtente(Utente utente) {
		callPost(pathUtente + "/aggiungiUtente", utente, Void.class);
	}

	@Override
	@Transactional(rollbackOn = DataAccessException.class)
	public Utente modificaUtente(Utente utente) {
		return callPost(pathUtente + "/modificaUtente", utente, Utente.class);
	}

	@Override
	@Transactional(rollbackOn = DataAccessException.class)
	public Utente eliminaUtente(long id) {
		Utente u = callPost(pathUtente + "/trovaPerId/" + id, id, Utente.class);
		u.setCancellato(true);
		return callPost(pathUtente + "/modificaUtente", u, Utente.class);
	}

	@Override
	public Utente findByEmail(String email) {
		return callPost(pathUtente + "/email/" + email, email, Utente.class);
	}

	@Override
	public Utente findById(long id) {
		return callPost(pathUtente + "/trovaPerId/" + id, id, Utente.class);
	}

	@Override
	public String disattivaAdmin(long id) {
		Utente u = callPost(pathUtente + "/trovaPerId/" + id, id, Utente.class);
		if (!u.getRuolo().equals(Ruolo.ADMIN)) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"l'utente ha ruolo " + u.getRuolo() + ", impossibile disattivare ruolo ADMIN");
		} else {
			u.setRuolo(Ruolo.CLIENTE);
			modificaUtente(u);
		}
		return u.getEmail();
	}

	@Override
	//torna il token
	public String login(LoginDTORequest request){
		return callPost(pathUtente + "/login", request, String.class);
	}
	public List<Utente> findByAllId(List<Long>ids){
		Utente [] utenti = callPost(pathUtente + "/trovaPerListaIds", ids, Utente[].class);
		return List.of(utenti);
	}


}
