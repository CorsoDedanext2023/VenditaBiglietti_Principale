package it.dedagroup.venditabiglietti.principal.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventiFiltratiDTORequest {
    CategoriaCriteriaDTORequest requestCategoria;
    ManifestazioneCriteriaDTORequest requestManifestazione;
    EventiCriteriaDTORequest requestEventi;
    LuogoCriteriaEventiDTORequest requestLuoghi;
}
