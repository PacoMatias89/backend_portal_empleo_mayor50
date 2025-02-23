package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationCreateDto;
import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationUpdateStatusDto;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.IJobApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JobApplicationControllerTest {

    @Mock
    private IJobApplicationService jobApplicationService;

    @InjectMocks
    private JobApplicationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createJobApplication_Success() {
        JobApplicationCreateDto jobApplicationCreateDto = new JobApplicationCreateDto();
        jobApplicationCreateDto.setJobOfferId(1L);

        UserEntityPrincipal userEntityPrincipal = mock(UserEntityPrincipal.class);
        when(userEntityPrincipal.getId()).thenReturn(1L);

        when(jobApplicationService.applyToJob(any(JobApplicationCreateDto.class), eq(1L), eq(1L)))
                .thenReturn(jobApplicationCreateDto);

        ResponseEntity<?> response = controller.createJobApplication(userEntityPrincipal, jobApplicationCreateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobApplicationCreateDto, response.getBody());
        verify(jobApplicationService, times(1)).applyToJob(jobApplicationCreateDto, 1L, 1L);
    }

    @Test
    void createJobApplication_Failure() {
        JobApplicationCreateDto jobApplicationCreateDto = new JobApplicationCreateDto();
        jobApplicationCreateDto.setJobOfferId(1L);

        UserEntityPrincipal userEntityPrincipal = mock(UserEntityPrincipal.class);
        when(userEntityPrincipal.getId()).thenReturn(1L);

        when(jobApplicationService.applyToJob(any(JobApplicationCreateDto.class), eq(1L), eq(1L)))
                .thenThrow(new RuntimeException("Job application creation failed"));

        ResponseEntity<?> response = controller.createJobApplication(userEntityPrincipal, jobApplicationCreateDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Job application creation failed", response.getBody());
        verify(jobApplicationService, times(1)).applyToJob(jobApplicationCreateDto, 1L, 1L);
    }

    @Test
    void updateJobApplicationStatus_Success() {
        JobApplicationUpdateStatusDto statusDto = new JobApplicationUpdateStatusDto();

        CompanyEntityPrincipal companyEntityPrincipal = mock(CompanyEntityPrincipal.class);
        when(companyEntityPrincipal.getId()).thenReturn(1L);
        when(jobApplicationService.updateJobApplicationStatus(statusDto, 1L, 1L)).thenReturn(statusDto);

        ResponseEntity<?> response = controller.updateJobApplicationStatus(companyEntityPrincipal, statusDto, 1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(statusDto, response.getBody());
        verify(jobApplicationService, times(1)).updateJobApplicationStatus(statusDto, 1L, 1L);
    }

    @Test
    void updateJobApplicationStatus_Failure() {
        JobApplicationUpdateStatusDto statusDto = new JobApplicationUpdateStatusDto();

        CompanyEntityPrincipal companyEntityPrincipal = mock(CompanyEntityPrincipal.class);
        when(companyEntityPrincipal.getId()).thenReturn(1L);
        when(jobApplicationService.updateJobApplicationStatus(statusDto, 1L, 1L))
                .thenThrow(new RuntimeException("Job application status update failed"));

        ResponseEntity<?> response = controller.updateJobApplicationStatus(companyEntityPrincipal, statusDto, 1L, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Job application status update failed", response.getBody());
        verify(jobApplicationService, times(1)).updateJobApplicationStatus(statusDto, 1L, 1L);
    }
}
