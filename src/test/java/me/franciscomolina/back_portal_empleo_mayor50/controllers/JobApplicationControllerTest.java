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
        // Mocks configurados correctamente
        UserEntityPrincipal userEntityPrincipal = mock(UserEntityPrincipal.class);
        JobApplicationCreateDto jobApplicationCreateDto = mock(JobApplicationCreateDto.class);

        // Configurar los mocks
        when(jobApplicationCreateDto.getJobOfferId()).thenReturn(1L);
        when(userEntityPrincipal.getId()).thenReturn(1L);
        when(jobApplicationService.applyToJob(jobApplicationCreateDto, 1L, 1L)).thenReturn(jobApplicationCreateDto);

        // Llamar al método que se está probando
        ResponseEntity<?> response = controller.createJobApplication(userEntityPrincipal, jobApplicationCreateDto);

        // Verificar la respuesta
        assertEquals(ResponseEntity.ok(jobApplicationCreateDto), response);
        assertEquals(jobApplicationCreateDto, response.getBody());
        verify(jobApplicationService, times(1)).applyToJob(jobApplicationCreateDto, 1L, 1L);
    }

    @Test
    void createJobApplication_Failure() {
        // Mocks configurados correctamente
        UserEntityPrincipal userEntityPrincipal = mock(UserEntityPrincipal.class);
        JobApplicationCreateDto jobApplicationCreateDto = mock(JobApplicationCreateDto.class);

        // Configurar los mocks
        when(jobApplicationCreateDto.getJobOfferId()).thenReturn(1L);
        when(userEntityPrincipal.getId()).thenReturn(1L);
        when(jobApplicationService.applyToJob(jobApplicationCreateDto, 1L, 1L)).thenThrow(new RuntimeException("Job application creation failed"));

        // Llamar al método que se está probando
        ResponseEntity<?> response = controller.createJobApplication(userEntityPrincipal, jobApplicationCreateDto);

        // Verificar la respuesta
        assertEquals(ResponseEntity.badRequest().body("Job application creation failed"), response);
        verify(jobApplicationService, times(1)).applyToJob(jobApplicationCreateDto, 1L, 1L);
    }


    @Test
    void updateJobApplicationStatus_Success() {
        // Mocks configurados correctamente
        CompanyEntityPrincipal companyEntityPrincipal = mock(CompanyEntityPrincipal.class);
        JobApplicationUpdateStatusDto status = mock(JobApplicationUpdateStatusDto.class);

        // Configurar los mocks
        when(companyEntityPrincipal.getId()).thenReturn(1L);
        when(jobApplicationService.updateJobApplicationStatus(status, 1L, 1L)).thenReturn(status);

        // Llamar al método que se está probando
        ResponseEntity<?> response = controller.updateJobApplicationStatus(companyEntityPrincipal, status, 1L);

        // Verificar la respuesta
        assertEquals(ResponseEntity.ok(status), response);
        assertEquals(status, response.getBody());
        verify(jobApplicationService, times(1)).updateJobApplicationStatus(status, 1L, 1L);
    }


    @Test
    void updateJobApplicationStatus_Failure() {
        // Mocks configurados correctamente
        CompanyEntityPrincipal companyEntityPrincipal = mock(CompanyEntityPrincipal.class);
        JobApplicationUpdateStatusDto status = mock(JobApplicationUpdateStatusDto.class);

        // Configurar los mocks
        when(companyEntityPrincipal.getId()).thenReturn(1L);
        when(jobApplicationService.updateJobApplicationStatus(status, 1L, 1L)).thenThrow(new RuntimeException("Job application status update failed"));

        // Llamar al método que se está probando
        ResponseEntity<?> response = controller.updateJobApplicationStatus(companyEntityPrincipal, status, 1L);

        // Verificar la respuesta
        assertEquals(ResponseEntity.badRequest().body("Job application status update failed"), response);
        verify(jobApplicationService, times(1)).updateJobApplicationStatus(status, 1L, 1L);
    }




}