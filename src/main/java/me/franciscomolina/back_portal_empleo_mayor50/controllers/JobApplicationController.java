package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import jakarta.validation.Valid;
import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationCreateDto;
import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationUpdateStatusDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.IJobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/company/job-application")
public class JobApplicationController {

    @Autowired
    private IJobApplicationService jobApplicationService;

    @PostMapping
    public ResponseEntity<?> createJobApplication(@Valid @AuthenticationPrincipal UserEntityPrincipal companyEntityPrincipal,
                                                  @RequestBody JobApplicationCreateDto jobApplicationCreateDto) {
        try {
            Long idUser = companyEntityPrincipal.getId();
            Long idJobOffer = jobApplicationCreateDto.getJobOfferId();
            JobApplicationCreateDto jobApplicationResponse = jobApplicationService.applyToJob(jobApplicationCreateDto, idJobOffer, idUser);
            return ResponseEntity.ok(jobApplicationResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getJobApplicationsCompany")
    public ResponseEntity<?> getJobApplicationsCompany(@AuthenticationPrincipal CompanyEntityPrincipal companyEntityPrincipal) {
        try {
            Long companyId = companyEntityPrincipal.getId();
            List<JobApplication> applications = jobApplicationService.getJobApplicationsByCompany(companyId);
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/updateJobApplicationStatus/{idJobOffer}/{idUser}")
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    public ResponseEntity<?> updateJobApplicationStatus(@Valid @AuthenticationPrincipal CompanyEntityPrincipal companyEntityPrincipal,
                                                        @RequestBody JobApplicationUpdateStatusDto statusDto,
                                                        @PathVariable Long idJobOffer,
                                                        @PathVariable Long idUser) {
        try {
            JobApplicationUpdateStatusDto jobApplicationResponse = jobApplicationService.updateJobApplicationStatus(statusDto, idJobOffer, idUser);
            return ResponseEntity.ok(jobApplicationResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado no válido");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/job-offer/{jobOfferId}/applications")
    public ResponseEntity<?> getApplicationsByJobOffer(@PathVariable Long jobOfferId) {
        try {
            List<JobApplication> applications = jobApplicationService.getApplicationsByJobOffer(jobOfferId);
            for (JobApplication application : applications) {
                System.out.println("✅ Candidatos obtenidos del backend: " + application.getUser().getName());
            }
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getApplicationsByUserId(@PathVariable Long userId) {
        try {
            List<JobApplication> applications = jobApplicationService.getJobApplication(userId);
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/by-date")
    public ResponseEntity<?> getJobApplicationsByDate(
            @AuthenticationPrincipal CompanyEntityPrincipal companyEntityPrincipal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        try {
            Long companyId = companyEntityPrincipal.getId();
            List<JobApplication> applications = jobApplicationService.getJobApplicationsByCompanyAndDateRange(companyId, startDate, endDate);
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/job-offers-with-applicants")
    public ResponseEntity<?> getJobOffersWithApplicants(@AuthenticationPrincipal CompanyEntityPrincipal companyEntityPrincipal) {
        try {
            Long companyId = companyEntityPrincipal.getId();
            List<JobOffer> jobOffers = jobApplicationService.getJobOffersWithApplicants(companyId);
            return ResponseEntity.ok(jobOffers);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}
