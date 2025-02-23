package me.franciscomolina.back_portal_empleo_mayor50.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonBackReference("user-favorites") // No se serializa el usuario aquí
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "job_offer_id")
    @JsonIgnoreProperties("favoriteJobs") // Al serializar jobOffer se ignora su lista de favoritos
    private JobOffer jobOffer;
}
