package me.franciscomolina.back_portal_empleo_mayor50.entities;

import com.fasterxml.jackson.annotation.*;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "jobOffer", fetch = FetchType.LAZY)
    private List<JobApplication> applications;

    @OneToMany(mappedBy = "jobOffer")
    private List<FavoritesJobs> favoriteJobs;

    @Transient
    @JsonView(Views.JobOfferDetail.class)
    private int numberOfApplications;

    @PostLoad
    public void postLoad() {
        if (applications != null) {
            this.numberOfApplications = applications.size();
        } else {
            this.numberOfApplications = 0;
        }
    }
}
