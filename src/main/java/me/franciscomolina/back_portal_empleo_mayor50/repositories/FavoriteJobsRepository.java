package me.franciscomolina.back_portal_empleo_mayor50.repositories;

import me.franciscomolina.back_portal_empleo_mayor50.entities.FavoritesJobs;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteJobsRepository extends JpaRepository<FavoritesJobs, Long> {
    // Buscar si un trabajo está marcado como favorito por un usuario
    boolean existsByUserAndJobOffer(UserEntity user, JobOffer jobOffer);

    // Buscar un favorito específico
    FavoritesJobs findByUserAndJobOffer(UserEntity user , JobOffer jobOffer);



    // Buscar todos los trabajos favoritos de un usuario a través de su ID
    @Query("SELECT f.jobOffer FROM FavoritesJobs f WHERE f.user.id = :userId")
    List<JobOffer> findAllFavoriteJobsByUserId(@Param("userId") Long userId);


    @Modifying
    @Query("DELETE FROM FavoritesJobs f WHERE f.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    // Buscar todos los trabajos favoritos de un usuario con los detalles completos
    /*@Query("SELECT f.jobOffer FROM FavoritesJobs f WHERE f.user = :user")
    List<JobOffer> findAllFavoriteJobsByUser(@Param("user") UserEntity user);*/

}