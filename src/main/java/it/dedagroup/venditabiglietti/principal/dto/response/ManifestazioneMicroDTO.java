package it.dedagroup.venditabiglietti.principal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ManifestazioneMicroDTO {

    private Long idCategoria;
    private long idUtente;
    private boolean isCancellato;
    private long version;
    @EqualsAndHashCode.Include
    private Long id;
    private String nome;
}
