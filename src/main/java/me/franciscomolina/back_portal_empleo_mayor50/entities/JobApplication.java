package me.franciscomolina.back_portal_empleo_mayor50.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import me.franciscomolina.back_portal_empleo_mayor50.model.JobApplicationStatus;
import me.franciscomolina.back_portal_empleo_mayor50.view.Views;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_applications")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.JobOfferDetail.class)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // Cambio a EAGER
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER) // Cambio a EAGER
    @JoinColumn(name = "job_offer_id")
    @JsonView(Views.JobOfferDetail.class)
    private JobOffer jobOffer;

    @Basic
    @Column(name = "created_at")
    @JsonView(Views.JobOfferDetail.class)
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @JsonView(Views.JobOfferDetail.class)
    private JobApplicationStatus status;
}
