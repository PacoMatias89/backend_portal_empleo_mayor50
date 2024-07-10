package me.franciscomolina.back_portal_empleo_mayor50.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotNull(message = "Tiene que ingresar un nombre")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotNull(message = "Tiene que ingresar los apellidos")
    @NotBlank(message = "Los apellidos no puedeacn estar vacíos")
    private String lastnames;

    @NotNull(message = "El email es obligatorio")
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    private String email;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate birthdate;

    @NotNull(message = "El password es obligatorio")
    @NotBlank(message = "El password no puede estar vacío")
    private String password;

    @NotNull(message = "La confirmación de la contraseña es obligatoria")
    @NotBlank(message = "La confirmación de la contraseña no puede estar vacía")
    private String confirmPassword;
}
