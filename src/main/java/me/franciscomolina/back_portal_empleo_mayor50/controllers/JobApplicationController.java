package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import jakarta.validation.Valid;
import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationCreateDto;
import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationUpdateStatusDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.IJobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company/job-application")
public class JobApplicationController {

    @Autowired
    private IJobApplicationService jobApplicationService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createJobApplication(@Valid @AuthenticationPrincipal UserEntityPrincipal companyEntityPrincipal,
                                                  @RequestBody JobApplicationCreateDto jobApplicationCreateDto) {
        try {
            Long idUser = companyEntityPrincipal.getId(); // Obtener el usuario desde el principal
            Long idJobOffer = jobApplicationCreateDto.getJobOfferId();
            JobApplicationCreateDto jobApplicationResponse = jobApplicationService.applyToJob(jobApplicationCreateDto, idJobOffer, idUser);
            return ResponseEntity.ok(jobApplicationResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getJobApplicationsCompany")
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
