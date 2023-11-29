package it.dedagroup.venditabiglietti.principal.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPSEDTORequest {
    @Positive(message = "Inserire un id valido positivo")
    @Min(value = 1, message = "Inserire un id maggiore o uguale a 1: ID PrezzoSettoreEvento")
    private long idPse;
    @Positive(message = "Inserire un id valido positivo")
    @Min(value = 1, message = "Inserire un id maggiore o uguale a 1: ID Evento")
    private long idEvento;
    @Positive(message = "Inserire un id valido positivo")
    @Min(value = 1, message = "Inserire un id maggiore o uguale a 1: ID Settore")
    private long idSettore;
    @Positive(message = "Inserire un prezzo valido positivo")
    @Min(value = 0, message = "Inserire un prezzo maggiore o uguale a 0: prezzo")
    private double prezzo;
}
