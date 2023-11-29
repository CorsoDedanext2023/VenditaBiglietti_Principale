package it.dedagroup.venditabiglietti.principal.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModificaUtenteLoggatoRequest {
	@Email
	private String emailAttuale;
	@NotBlank(message="Il campo password non può essere vuoto")
	@Size(min = 8, message = "La password deve essere almeno di otto caratteri.")
	@Pattern(regexp = "^(?=.*[!@#&()–_[{}]:;',?/*~$^+=<>]).*$", message = "La password deve contenere almeno un simbolo")
	@Pattern(regexp = "^(?=.*[0-9]).*$", message = "La password deve contenere almeno un numero")
	@Pattern(regexp = "^(?=.*[a-z]).*$", message = "La password deve contenere almeno una lettera minuscola")
	@Pattern(regexp = "^(?=.*[A-Z]).*$", message = "La password deve contenere almeno una lettera maiuscola")
	private String passwordAttuale;
	@Email
	private String nuovaEmail;
	@NotBlank(message="Il campo password non può essere vuoto")
	@Size(min = 8, message = "La password deve essere almeno di otto caratteri.")
	@Pattern(regexp = "^(?=.*[!@#&()–_[{}]:;',?/*~$^+=<>]).*$", message = "La password deve contenere almeno un simbolo")
	@Pattern(regexp = "^(?=.*[0-9]).*$", message = "La password deve contenere almeno un numero")
	@Pattern(regexp = "^(?=.*[a-z]).*$", message = "La password deve contenere almeno una lettera minuscola")
	@Pattern(regexp = "^(?=.*[A-Z]).*$", message = "La password deve contenere almeno una lettera maiuscola")
	private String nuovaPassword;
	@Pattern(regexp = "\\d+", message = "il numero di telefono deve essere composto da solo cifre")
	private String nuovoTelefono;
}
