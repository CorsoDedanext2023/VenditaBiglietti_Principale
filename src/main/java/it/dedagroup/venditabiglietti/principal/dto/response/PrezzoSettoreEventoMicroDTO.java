package it.dedagroup.venditabiglietti.principal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrezzoSettoreEventoMicroDTO {
    private long id;
    private long idSettore;
    private long idEvento;
    private double prezzo;
    private boolean isCancellato;
    private long version;
}
