package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.entities.FavoritesJobs;

import java.util.List;

public interface IFavoriteJobService {

    String addFavoriteJob(Long userId, Long jobOfferId);

    String deleteFavoriteJob(Long userId, Long jobOfferId);


    List<FavoritesJobs>getFavoriteJobs();
}
