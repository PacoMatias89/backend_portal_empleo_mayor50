package me.franciscomolina.back_portal_empleo_mayor50.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "favorites_jobs")
public class FavoritesJobs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "job_offer_id")
    private JobOffer jobOffer;



}
