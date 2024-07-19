package me.franciscomolina.back_portal_empleo_mayor50.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_offers")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")


public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "title")
    @NotBlank(message = "El título no puede estar vacío")
    @NotNull(message = "El título es obligatorio")
    private String title;

    @Basic
    @Column(name = "description")
    @NotBlank(message = "La descripción no puede estar vacía")
    @NotNull(message = "La descripción es obligatoria")
    private String description;

    @Basic
    @Column(name = "salary")
    @NotNull(message = "El salario es obligatorio")
    private Double salary;

    @Basic
    @Column(name = "requirements")
    @NotBlank(message = "Los requisitos no pueden estar vacíos")
    @NotNull(message = "Los requisitos son obligatorios")
    private String requirements;

    @Basic
    @Column(name = "location")
    @NotBlank(message = "La localización no puede estar vacía")
    @NotNull(message = "La localización es obligatoria")
    private String location;

    @Basic
    @Column(name = "created_at")
    private LocalDate createdAt;

    //Saber las personas que han aplicado a la oferta JobOffer
    @Getter
    @OneToMany(mappedBy = "jobOffer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<JobApplication> applications;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonBackReference
    private Company company;

    @Transient
    private int numberOfApplications;



}
