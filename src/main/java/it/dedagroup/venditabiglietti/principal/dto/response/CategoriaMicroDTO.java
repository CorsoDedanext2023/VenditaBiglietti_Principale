package it.dedagroup.venditabiglietti.principal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoriaMicroDTO {

    private boolean isCancellato;
    private long version;
    @EqualsAndHashCode.Include
    private long id;
    private String nome;

}
