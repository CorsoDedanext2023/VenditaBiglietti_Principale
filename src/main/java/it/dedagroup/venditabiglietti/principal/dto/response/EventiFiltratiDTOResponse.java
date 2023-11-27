package it.dedagroup.venditabiglietti.principal.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventiFiltratiDTOResponse {
    String nomeManifestazione;
    String nomeCategoria;
    String descrizioneEvento;
    LocalDate dataEvento;
    LocalTime oraEvento;
    String comune;
    String provincia;
}
