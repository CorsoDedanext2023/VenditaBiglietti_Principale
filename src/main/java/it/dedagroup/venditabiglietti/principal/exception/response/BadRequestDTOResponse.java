package it.dedagroup.venditabiglietti.principal.exception.response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BadRequestDTOResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
