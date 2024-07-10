package me.franciscomolina.back_portal_empleo_mayor50.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import me.franciscomolina.back_portal_empleo_mayor50.model.Role;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*Datos para que la empresa pueda acceder al portal*/
    @Basic
    @Column(name = "name")
    @NotNull(message = "Tiene que ingresar un nombre de usuario")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "[0-9A-Za-zAÑáéióúAEÍO6¡!¿?@$%()=+-€/.,~\\s]{1,50}", message = "El nombre debe contener letras, espacios y opcionalmente números.")
    private String name;

    @Basic
    @Column(name="lastname")
    @NotNull(message = "Tiene que ingresar los apellidos")
    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Pattern(regexp = "[0-9A-Za-zAÑáéióúAEÍO6¡!¿?@$%()=+-€/.,~\\s]{1,50}", message = "El apellido debe contener letras, espacios y opcionalmente números.")
    private String lastname;

    @Basic
    @Email(message = "El email debe ser válido")
    @Column(name = "email", unique = true)
    @NotNull(message = "El email es obligatorio")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @Basic
    @Column(name = "password")
    @NotNull(message = "El password es obligatorio")
    @NotBlank(message = "El password no puede estar vacío")
    private String password;

    @Transient
    @NotNull(message = "La confirmación de la  password es obligatorio")
    @NotBlank(message = "La confirmación de la  password no puede estar vacío")
    private String confirmPasswordCompany;


    /*Datos de la empresa*/
    @Basic
    @Column(name = "company_name")
    @NotNull(message = "Tiene que ingresar un nombre de empresa")
    @NotBlank(message = "El nombre de la empresa no puede estar vacío")
    @Pattern(regexp = "[0-9A-Za-zAÑáéióúAEÍO6¡!¿?@$%()=+-€/.,~\\s]{1,50}", message = "El nombre debe contener letras, espacios y opcionalmente números.")
    private String companyName;

    @Basic
    @Column(name="contact_person")
    @NotNull(message = "El teléfono de contacto no puede ser nulo")
    @NotBlank(message = "El teléfono de contacto no puede estar vacío")
    @Pattern(regexp = "[0-9]{9}", message = "El teléfono de contacto debe contener 9 dígitos")
    private String phoneContact;

    @Basic
    @Column(name="cif_company")
    @NotNull(message = "El CIF o NIF de la empresa no puede ser nulo")
    @NotBlank(message = "El CIF o NIF de la empresa no puede estar vacío")
    @Pattern(regexp = "[0-9A-Za-z]{9}", message = "El CIF o NIF de la empresa debe contener 9 caracteres")
    private String cifCompany;

    @Basic
    @Column(name="ETT")
    @NotNull(message = "El ETT de la empresa no puede ser nulo")
    private Boolean isEtt;

    @Basic
    @Column(name="description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private Role role;

    @Basic
    @Column(name = "created_at")
    private LocalDate createdAt;


    @Transient
    private String token;


}
