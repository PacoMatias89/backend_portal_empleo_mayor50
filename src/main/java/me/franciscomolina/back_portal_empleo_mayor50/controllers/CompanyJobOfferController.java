package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import me.franciscomolina.back_portal_empleo_mayor50.dto.JobOfferDto;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.IJobOfferService;
import me.franciscomolina.back_portal_empleo_mayor50.view.Views;
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
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getJobOfferById/{id}")
    @JsonView(Views.JobOfferDetail.class)
    public ResponseEntity<?> getJobOfferById(@PathVariable Long id) {
        return ResponseEntity.ok(jobOfferService.getJobOfferById(id));
    }

    @GetMapping("/getAllJobOffer")
    @JsonView(Views.JobOfferDetail.class)
    public ResponseEntity<?> getJobOffers() {

        return ResponseEntity.ok(jobOfferService.getJobOffers());
    }

    @GetMapping("/getJobOfferByIdCompany/{id}")
    @JsonView(Views.JobOfferDetail.class)
    public ResponseEntity<?> getJobOfferByIdCompany(@PathVariable Long id) {
        return ResponseEntity.ok(jobOfferService.getJobOfferByIdCompany(id));
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateJobOffer(@AuthenticationPrincipal CompanyEntityPrincipal company, @RequestBody JobOfferDto jobOfferDto) {
        try {
            Long id = company.getId();
            jobOfferService.updateJobOffer(id, jobOfferDto);
            return ResponseEntity.ok("Oferta de trabajo actualizada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteJobOffer(@AuthenticationPrincipal CompanyEntityPrincipal company, @RequestParam Long id) {
        try {
            jobOfferService.deleteJobOffer(id);
            return ResponseEntity.ok("Oferta de trabajo eliminada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
