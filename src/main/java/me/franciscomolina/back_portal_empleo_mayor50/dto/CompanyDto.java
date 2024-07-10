package me.franciscomolina.back_portal_empleo_mayor50.dto;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    /*Datos para que la empresa pueda acceder al portal*/

    @NotNull(message = "Tiene que ingresar un nombre de usuario")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "[0-9A-Za-zAÑáéióúAEÍO6¡!¿?@$%()=+-€/.,~\\s]{1,50}", message = "El nombre debe contener letras, espacios y opcionalmente números.")
    private String name;


    @NotNull(message = "Tiene que ingresar los apellidos")
    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Pattern(regexp = "[0-9A-Za-zAÑáéióúAEÍO6¡!¿?@$%()=+-€/.,~\\s]{1,50}", message = "El apellido debe contener letras, espacios y opcionalmente números.")
    private String lastname;


    @NotNull(message = "El email es obligatorio")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotNull(message = "El password es obligatorio")
    @NotBlank(message = "El password no puede estar vacío")
    private String password;


    @NotNull(message = "La confirmación de la  password es obligatorio")
    @NotBlank(message = "La confirmación de la  password no puede estar vacío")
    private String confirmPasswordCompany;


    /*Datos de la empresa*/

    @NotNull(message = "Tiene que ingresar un nombre de empresa")
    @NotBlank(message = "El nombre de la empresa no puede estar vacío")
    @Pattern(regexp = "[0-9A-Za-zAÑáéióúAEÍO6¡!¿?@$%()=+-€/.,~\\s]{1,50}", message = "El nombre debe contener letras, espacios y opcionalmente números.")
    private String companyName;

    @NotNull(message = "El teléfono de contacto no puede ser nulo")
    @NotBlank(message = "El teléfono de contacto no puede estar vacío")
    @Pattern(regexp = "[0-9]{9}", message = "El teléfono de contacto debe contener 9 dígitos")
    private String phoneContact;


    @NotNull(message = "El CIF o NIF de la empresa no puede ser nulo")
    @NotBlank(message = "El CIF o NIF de la empresa no puede estar vacío")
    @Pattern(regexp = "[0-9A-Za-z]{9}", message = "El CIF o NIF de la empresa debe contener 9 caracteres")
    private String cifCompany;


    @NotNull(message = "El ETT de la empresa no puede ser nulo")
    private Boolean isEtt;

    @Basic
    @Column(name="description")
    private String description;

}
