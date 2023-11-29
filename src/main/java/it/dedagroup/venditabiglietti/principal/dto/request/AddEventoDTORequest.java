package it.dedagroup.venditabiglietti.principal.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddEventoDTORequest {
	@NotBlank(message = "Valorizzare il campo della data")
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}+$", message = "Inserire una data valida")
	private String data;
	@NotBlank(message = "Valorizzare il campo dell'ora")
	@Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Inserire unorario valido")
	private String ora;
	@NotBlank(message = "Valorizzare il campo della descrizione")
	private String descrizione;
	@Positive(message = "Inserire un id valido positivo")
	@Min(value = 1, message = "Inserire un id maggiore o uguale a 1: ID Manifestazione")
	private long idManifestazione;
	@Positive(message = "Inserire un id valido positivo")
	@Min(value = 1, message = "Inserire un id maggiore o uguale a 1: ID Luogo")
	private long idLuogo;
}
