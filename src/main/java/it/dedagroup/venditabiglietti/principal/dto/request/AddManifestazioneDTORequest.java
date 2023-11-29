package it.dedagroup.venditabiglietti.principal.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddManifestazioneDTORequest {
    @NotBlank(message = "Valorizzare il campo nome manifestazione")
    private String nome;
    @Positive(message = "Inserire un id valido positivo")
    @Min(value = 1, message = "Inserire un id maggiore o uguale a 1: ID Categoria")
    private long idCategoria;
    private long idUtente;
}
