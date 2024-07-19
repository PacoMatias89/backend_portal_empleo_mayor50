package me.franciscomolina.back_portal_empleo_mayor50.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import me.franciscomolina.back_portal_empleo_mayor50.model.Role;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "name")
    @NotNull(message = "Tiene que ingresar un nombre")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "[0-9A-Za-zAÑáéióúAEÍO6¡!¿?@$%()=+-€/.,~\\s]{1,50}", message = "El nombre debe contener letras, espacios y opcionalmente números.")
    private String name;

    @Basic
    @Column(name = "lastnames")
    @NotNull(message = "Tiene que ingresar los apellidos")
    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Pattern(regexp = "[0-9A-Za-zAÑáéióúAEÍO6¡!¿?@$%()=+-€/.,~\\s]{1,50}", message = "El apellido debe contener letras, espacios y opcionalmente números.")
    private String lastnames;

    @Basic
    @Column(name = "email", unique = true)
    @NotNull(message = "El email es obligatorio")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @Basic
    @Column(name = "birthdate")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate birthdate;

    @Basic
    @Column(name = "password")
    @NotNull(message = "El password es obligatorio")
    @NotBlank(message = "El password no puede estar vacío")
    private String password;

    @Transient
    @NotNull(message = "La confirmación de la contraseña es obligatoria")
    @NotBlank(message = "La confirmación de la contraseña no puede estar vacía")
    private String confirmPassword;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private Role role;

    @Transient
    private String token;

    @Basic
    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user-experiences")
    @JsonIgnore
    private List<WorkExperience> workExperiences;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user-applications")
    @JsonIgnore
    private List<JobApplication> jobApplications;

    @Transient
    @JsonIgnore
    private String totalExperience;
}
