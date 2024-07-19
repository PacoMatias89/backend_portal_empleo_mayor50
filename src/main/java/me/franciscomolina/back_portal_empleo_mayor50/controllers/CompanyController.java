package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import me.franciscomolina.back_portal_empleo_mayor50.dto.CompanyDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.ICompanyService;
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
}
