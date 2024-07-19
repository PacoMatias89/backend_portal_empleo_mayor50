package me.franciscomolina.back_portal_empleo_mayor50.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import me.franciscomolina.back_portal_empleo_mayor50.model.Role;
import me.franciscomolina.back_portal_empleo_mayor50.view.Views;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.UserSignInView.class) // Aplicar vista nueva
    private Long id;

    @Basic
    @Column(name = "name")
    @JsonView(Views.UserSignInView.class)
    private String name;

    @Basic
    @Column(name = "lastnames")
    @JsonView(Views.UserSignInView.class)
    private String lastnames;

    @Basic
    @Column(name = "email", unique = true)
    @JsonView(Views.UserSignInView.class)
    private String email;

    @Basic
    @Column(name = "birthdate")
    @JsonView(Views.UserSignInView.class)
    private LocalDate birthdate;

    @Basic
    @Column(name = "password")
    @JsonView(Views.UserSignInView.class)
    private String password;

    @Transient
    @JsonView(Views.UserSignInView.class)
    private String confirmPassword;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    @JsonView(Views.UserSignInView.class)
    private Role role;

    @Transient
    @JsonView(Views.UserSignInView.class)
    private String token;

    @Basic
    @Column(name = "created_at")
    @JsonView(Views.UserSignInView.class)
    private LocalDate createdAt;

    // Excluir workExperiences y jobApplications de la vista UserSignInView
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonView(Views.JobOfferDetail.class) // Mantener la vista existente para otros casos
    private List<WorkExperience> workExperiences;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonView(Views.JobOfferDetail.class) // Mantener la vista existente para otros casos
    private List<JobApplication> jobApplications;

    @Transient
    @JsonView(Views.UserSignInView.class)
    private String totalExperience;
}