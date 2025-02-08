package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.entities.FavoritesJobs;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.FavoriteJobsRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobOfferRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteJobsService implements IFavoriteJobService{

    @Autowired
    private FavoriteJobsRepository favoriteJobsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Override
    public String addFavoriteJob(Long userId, Long jobOfferId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el usuario " ));
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la oferta de trabajo " ));

        if (user == null || jobOffer == null) {
            return "No se ha encontrado el usuario o la oferta de trabajo";
        }

        //verirficar si el trabajo ya está marcado como favorito
        if (favoriteJobsRepository.existsByUserAndJobOffer(user, jobOffer)) {
            return "El trabajo ya está marcado como favorito";
        }

        //crear el favorito
        FavoritesJobs favorite = new FavoritesJobs();
        favorite.setUser(user);
        favorite.setJobOffer(jobOffer);
        favoriteJobsRepository.save(favorite);
        return "Job added to favorites";
    }


    @Override
    public String deleteFavoriteJob(Long userId, Long jobOfferId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el usuario " ));
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la oferta de trabajo " ));

        FavoritesJobs favorite = favoriteJobsRepository.findByUserAndJobOffer(user, jobOffer);

        if (favorite != null) {
            favoriteJobsRepository.delete(favorite);
            return "Se ha eliminado el trabajo de favoritos";
        }
        return "El trabajo no está marcado como favorito";
    }

    @Override
    public List<FavoritesJobs> getFavoriteJobs() {


        return favoriteJobsRepository.findAll();

    }


}
