package it.dedagroup.venditabiglietti.principal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventoMicroDTO {
    private boolean isCancellato;
    private long idLuogo;
    private long idManifestazione;
    private long version;
    private LocalDate data;
    private LocalTime ora;
    @EqualsAndHashCode.Include
    private long id;
    private String descrizione;
}
