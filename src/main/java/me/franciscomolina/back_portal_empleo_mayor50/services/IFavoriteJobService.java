package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.entities.FavoritesJobs;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;


import java.util.List;

public interface IFavoriteJobService {

    String addFavoriteJob(Long userId, Long jobOfferId);

    String deleteFavoriteJob(Long userId, Long jobOfferId);


    List<FavoritesJobs>getFavoriteJobs();


    List<JobOffer> getFavoriteJobs(Long userId);
}