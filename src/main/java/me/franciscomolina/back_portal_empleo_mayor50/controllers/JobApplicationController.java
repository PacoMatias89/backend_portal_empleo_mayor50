package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import jakarta.validation.Valid;
import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationDTO;
import me.franciscomolina.back_portal_empleo_mayor50.services.IJobApplicationService;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company/job-application")
public class JobApplicationController {

    @Autowired
    private IJobApplicationService jobApplicationService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createJobApplication(@Valid @AuthenticationPrincipal UserEntityPrincipal userEntityPrincipal, @RequestBody JobApplicationDTO jobApplicationDTO) {
        try {
            Long idUser = userEntityPrincipal.getId();
            Long idJobOffer = jobApplicationDTO.getJobOfferId();
            JobApplicationDTO jobApplicationResponse = jobApplicationService.applyToJob(jobApplicationDTO, idJobOffer, idUser);
            return ResponseEntity.ok(jobApplicationResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
