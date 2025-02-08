package me.franciscomolina.back_portal_empleo_mayor50.repositories;

import me.franciscomolina.back_portal_empleo_mayor50.entities.FavoritesJobs;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteJobsRepository extends JpaRepository<FavoritesJobs, Long> {

    // Buscar si un trabajo está marcado como favorito por un usuario
    boolean existsByUserAndJobOffer(UserEntity user, JobOffer jobOffer);

    // Buscar un favorito específico
    FavoritesJobs findByUserAndJobOffer(UserEntity user , JobOffer jobOffer);
}
