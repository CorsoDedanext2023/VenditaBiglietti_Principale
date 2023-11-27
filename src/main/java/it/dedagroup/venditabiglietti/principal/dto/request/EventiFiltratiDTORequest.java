package it.dedagroup.venditabiglietti.principal.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventiFiltratiDTORequest {
    String nomeCategoria;
    String nomeManifestazione;
    EventiCriteriaDTORequest requestEventi;
    LuogoCriteriaEventiDTORequest requestLuoghi;
}
