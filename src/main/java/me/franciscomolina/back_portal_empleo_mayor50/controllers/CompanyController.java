package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import me.franciscomolina.back_portal_empleo_mayor50.dto.CompanyDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.ICompanyService;
import me.franciscomolina.back_portal_empleo_mayor50.services.IJobOfferService;
import me.franciscomolina.back_portal_empleo_mayor50.view.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private IJobOfferService jobOfferService;

    @PutMapping("/update")
    public ResponseEntity<?> updateCompany(@AuthenticationPrincipal CompanyEntityPrincipal principal, @RequestBody CompanyDto companyDto) {
        if (principal == null) {
            return new ResponseEntity<>("Principal is null", HttpStatus.UNAUTHORIZED);
        }

        try {
            Long id = principal.getId();
            companyService.editCompany(id, companyDto);
            return new ResponseEntity<>("La empresa ha sido actualizada correctamente", HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/jobOffers")
    @JsonView(Views.JobOfferDetail.class)
    public ResponseEntity<?> getJobApplications(@Valid @AuthenticationPrincipal CompanyEntityPrincipal principal) {
        if (principal == null) {
            return new ResponseEntity<>("Principal is null", HttpStatus.UNAUTHORIZED);
        }

        try {
            Long id = principal.getId();
            List<JobOffer> jobApplications = companyService.getJobOffers(id);
            return new ResponseEntity<>(jobApplications, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/jobOffersCount")
    public ResponseEntity<?> getJobOffersCount(@AuthenticationPrincipal CompanyEntityPrincipal principal) {
        if (principal == null) {
            return new ResponseEntity<>("Principal is null", HttpStatus.UNAUTHORIZED);
        }

        try {
            Long companyId = principal.getId();
            int jobOffersCount = jobOfferService.countJobOffersByCompanyId(companyId); // Llamamos al servicio
            return new ResponseEntity<>(jobOffersCount, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/applications")
    public ResponseEntity<?> getTotalApplications(@AuthenticationPrincipal CompanyEntityPrincipal principal) {
        if (principal == null) {
            return new ResponseEntity<>("Principal is null", HttpStatus.UNAUTHORIZED);
        }

        try {
            Long id = principal.getId();
            int totalApplications = jobOfferService.countApplicationsByCompanyId(id); // Llamamos al servicio
            return new ResponseEntity<>(totalApplications, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/totalViews")
    public ResponseEntity<?> getTotalViews(@AuthenticationPrincipal CompanyEntityPrincipal principal) {
        if (principal == null) {
            return new ResponseEntity<>("Principal is null", HttpStatus.UNAUTHORIZED);
        }

        try {
            Long id = principal.getId();
            int totalViews = jobOfferService.getTotalViewsByCompany(id);
            return new ResponseEntity<>(totalViews, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/jobOffers/{jobId}/incrementViews")
    public ResponseEntity<?> incrementJobOfferViews(@AuthenticationPrincipal UserEntityPrincipal principal, @PathVariable Long jobId) {
        if (principal == null) {
            return new ResponseEntity<>("Usuario no autenticado, por favor inicie sesión.", HttpStatus.UNAUTHORIZED);
        }

        try {
            Long id = principal.getId();
            // Verificar si el jobId existe en la base de datos antes de incrementar vistas
            boolean jobExists = jobOfferService.existsById(jobId);
            if (!jobExists) {
                return new ResponseEntity<>("La oferta de empleo no existe.", HttpStatus.NOT_FOUND);
            }
            jobOfferService.incrementViews(jobId); // Llamar al servicio que incrementa el contador de vistas
            return new ResponseEntity<>("Vista incrementada correctamente", HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("Error al obtener usuario: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Hubo un error al incrementar las vistas: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
