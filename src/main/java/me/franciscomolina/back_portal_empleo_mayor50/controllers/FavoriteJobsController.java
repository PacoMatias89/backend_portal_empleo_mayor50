package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import lombok.RequiredArgsConstructor;
import me.franciscomolina.back_portal_empleo_mayor50.dto.FavoriteJobsDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.FavoritesJobs;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.IFavoriteJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/favorite-job")
@RequiredArgsConstructor
public class FavoriteJobsController {

    @Autowired
    private final IFavoriteJobService favoriteJobService;

    @PostMapping
    public ResponseEntity<String> addFavoriteJob(@AuthenticationPrincipal UserEntityPrincipal user, @RequestBody FavoriteJobsDto favoriteJobsDto) {
        try {
            String response = favoriteJobService.addFavoriteJob(user.getId(), favoriteJobsDto.getJobOfferId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al agregar oferta a favoritos: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllFavoriteJobs(@AuthenticationPrincipal UserEntityPrincipal user) {
        try {

            List<FavoritesJobs> favoriteJobs = favoriteJobService.getFavoriteJobs();
            return ResponseEntity.ok(favoriteJobs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener las ofertas favoritas: " + e.getMessage());
        }
    }


    @DeleteMapping("/{jobOfferId}")
    public ResponseEntity<String> deleteFavoriteJob(@AuthenticationPrincipal UserEntityPrincipal user, @PathVariable Long jobOfferId) {
        try {
            String response = favoriteJobService.deleteFavoriteJob(user.getId(), jobOfferId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar oferta de favoritos: " + e.getMessage());
        }
    }
}
