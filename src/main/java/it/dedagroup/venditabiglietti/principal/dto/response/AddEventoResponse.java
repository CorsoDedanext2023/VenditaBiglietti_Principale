package it.dedagroup.venditabiglietti.principal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddEventoResponse {
    private long id;
    private LocalDate data;
    private LocalTime ora;
    private String descrizione;
    private String nomeManifestazione;
    private String viaLuogo;
}
