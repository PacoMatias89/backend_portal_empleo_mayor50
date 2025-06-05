package me.franciscomolina.back_portal_empleo_mayor50.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
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
@Table(name = "company")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.CompanyBasic.class)
    private Long id;

    @Basic
    @Column(name = "name")
    @JsonView(Views.CompanyBasic.class)
    private String name;

    @Basic
    @Column(name="lastname")
    @JsonView(Views.CompanyBasic.class)
    private String lastname;

    @Basic
    @Column(name = "email", unique = true)
    @JsonView(Views.CompanyBasic.class)
    private String email;

    @Basic
    @Column(name = "password")
    @JsonView(Views.CompanyBasic.class)
    private String password;

    @Transient
    @JsonView(Views.CompanyBasic.class)
    private String confirmPasswordCompany;

    @Basic
    @Column(name = "company_name")
    @JsonView(Views.CompanyBasic.class)
    private String companyName;

    @Basic
    @Column(name="contact_person")
    @JsonView(Views.CompanyBasic.class)
    private String phoneContact;

    @Basic
    @Column(name="cif_company")
    @JsonView(Views.CompanyBasic.class)
    private String cifCompany;

    @Basic
    @Column(name="ETT")
    @JsonView(Views.CompanyBasic.class)
    private Boolean isEtt;

    @Basic
    @Column(name="description")
    @JsonView(Views.CompanyBasic.class)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    @JsonView(Views.CompanyBasic.class)
    private Role role;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonView(Views.JobOfferDetail.class)
    private List<JobOffer> jobOffers;

    @Basic
    @Column(name = "created_at")
    @JsonView(Views.CompanyBasic.class)
    private LocalDate createdAt;

    @Transient
    @JsonView(Views.CompanyBasic.class)
    private String token;

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmPasswordCompany='" + confirmPasswordCompany + '\'' +
                ", companyName='" + companyName + '\'' +
                ", phoneContact='" + phoneContact + '\'' +
                ", cifCompany='" + cifCompany + '\'' +
                ", isEtt=" + isEtt +
                ", description='" + description + '\'' +
                ", role=" + role +
                ", jobOffers=" + jobOffers +
                ", createdAt=" + createdAt +
                ", token='" + token + '\'' +
                '}';
    }
}
