package it.dedagroup.venditabiglietti.principal.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDTORequest {

    @NotBlank(message="Il campo username non può essere vuoto")
    @Email
    private String email;
    @NotBlank(message="Il campo password non  può essere vuoto")
    @Size(min = 8, message = "La password deve essere almeno di otto caratteri.")
    @Pattern(regexp = "^(?=.*[!@#&()–_[{}]:;',?/*~$^+=<>]).*$", message = "La password deve contenere almeno un simbolo")
    @Pattern(regexp = "^(?=.*[0-9]).*$", message = "La password deve contenere almeno un numero")
    @Pattern(regexp = "^(?=.*[a-z]).*$", message = "La password deve contenere almeno una lettera minuscola")
    @Pattern(regexp = "^(?=.*[A-Z]).*$", message = "La password deve contenere almeno una lettera maiuscola")
    private String password;

}
