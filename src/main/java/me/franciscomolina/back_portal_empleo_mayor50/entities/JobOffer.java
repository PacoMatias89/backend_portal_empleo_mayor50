package me.franciscomolina.back_portal_empleo_mayor50.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;
import me.franciscomolina.back_portal_empleo_mayor50.view.Views;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_offers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.JobOfferDetail.class)
    private Long id;

    @Basic
    @Column(name = "title")
    @JsonView(Views.JobOfferDetail.class)
    private String title;

    @Basic
    @Column(name = "name_company")
    @JsonView(Views.JobOfferDetail.class)
    private String nameCompany;

    @Basic
    @Column(name = "description")
    @JsonView(Views.JobOfferDetail.class)
    private String description;

    @Basic
    @Column(name = "salary")
    @JsonView(Views.JobOfferDetail.class)
    private Double salary;

    @Basic
    @Column(name = "requirements")
    @JsonView(Views.JobOfferDetail.class)
    private String requirements;

    @Basic
    @Column(name = "location")
    @JsonView(Views.JobOfferDetail.class)
    private String location;

    @Basic
    @Column(name = "created_at")
    @JsonView(Views.JobOfferDetail.class)
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.EAGER) // Cambio a EAGER
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties("jobOffers")
    private Company company;

    @OneToMany(mappedBy = "jobOffer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<JobApplication> applications;

    @OneToMany(mappedBy = "jobOffer", fetch = FetchType.LAZY)
    @JsonBackReference("jobOffer-favorites") // Evita referencia circular
    private List<FavoritesJobs> favoriteJobs;

    @Transient
    @JsonView(Views.JobOfferDetail.class)
    private int numberOfApplications;

    @Transient
    private long totalApplicants;

    @Basic
    @Column(name = "views", nullable = false)
    @JsonView(Views.JobOfferDetail.class)
    private Integer views = 0;

    @PrePersist
    public void setDefaultValues() {
        if (this.views == null) {
            this.views = 0;
        }
    }

    @PostLoad
    public void postLoad() {
        this.numberOfApplications = (applications != null) ? applications.size() : 0;
    }
}
