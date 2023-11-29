package it.dedagroup.venditabiglietti.principal.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
public class EventoDTOResponse {
	private long id;
	private LocalDate data;
	private LocalTime ora;
	private String descrizione;
	private long idManifestazione;
	private long idLuogo;
}
