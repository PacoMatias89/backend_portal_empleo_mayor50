package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import jakarta.validation.Valid;
import me.franciscomolina.back_portal_empleo_mayor50.dto.JobOfferDto;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.IJobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company/job-offers")
public class CompanyJobOfferController {

    @Autowired
    private IJobOfferService jobOfferService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createJobOffer(@Valid @AuthenticationPrincipal CompanyEntityPrincipal company, @RequestBody JobOfferDto jobOfferDto) {
        try {
            Long id = company.getId();

            jobOfferService.createJobOffer(jobOfferDto, id);
            return ResponseEntity.ok("Oferta de trabajo creada correctamente");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllJobOffer")
    public ResponseEntity<?> getJobOffers() {
        //TODO: Que no se vean las personas que han aplicado a la oferta de trabajo

        return ResponseEntity.ok(jobOfferService.getJobOffers());
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateJobOffer(@AuthenticationPrincipal CompanyEntityPrincipal company, @RequestBody JobOfferDto jobOfferDto) {
        try {
            Long id = company.getId();
            jobOfferService.updateJobOffer(id, jobOfferDto);
            return ResponseEntity.ok("Oferta de trabajo actualizada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
