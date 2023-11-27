package it.dedagroup.venditabiglietti.principal.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventiCriteriaDTORequest {
    LocalDate data;
    String descrizione;
}
