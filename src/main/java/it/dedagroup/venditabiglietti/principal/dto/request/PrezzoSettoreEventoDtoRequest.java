package it.dedagroup.venditabiglietti.principal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrezzoSettoreEventoDtoRequest {
    private long idSettore;
    private long idEvento;
    private double prezzo;
}
