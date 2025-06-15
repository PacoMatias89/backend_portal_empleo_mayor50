package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import me.franciscomolina.back_portal_empleo_mayor50.dto.CompanyDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.ICompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class CompanyControllerTest {

    @Mock
    private ICompanyService companyService;

    @Mock
    private CompanyEntityPrincipal companyEntityPrincipal;

    @InjectMocks
    private CompanyController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void patchCompany_Success() {
        Long id = 1L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("companyName", "Nueva Empresa");

        // No lanzamos excepción, método exitoso
        doNothing().when(companyService).updateCompanyPartial(id, updates);

        ResponseEntity<?> response = controller.patchCompany(id, updates);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Empresa actualizada parcialmente", response.getBody());
        verify(companyService, times(1)).updateCompanyPartial(id, updates);
    }

    @Test
    void patchCompany_CompanyNotFound() {
        Long id = 1L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "email@nuevo.com");

        doThrow(new UsernameNotFoundException("Company not found")).when(companyService).updateCompanyPartial(id, updates);

        ResponseEntity<?> response = controller.patchCompany(id, updates);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Company not found", response.getBody());
        verify(companyService, times(1)).updateCompanyPartial(id, updates);
    }


    @Test
    void getJobApplications_Success() {
        Long id = 1L;
        List<JobOffer> jobApplications = List.of(new JobOffer(), new JobOffer());
        when(companyEntityPrincipal.getId()).thenReturn(id);
        when(companyService.getJobOffers(id)).thenReturn(jobApplications);

        ResponseEntity<?> response = controller.getJobApplications(companyEntityPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jobApplications, response.getBody());
        verify(companyService, times(1)).getJobOffers(id);
    }

    @Test
    void getJobApplications_Unauthorized() {
        ResponseEntity<?> response = controller.getJobApplications(null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Principal is null", response.getBody());
        verify(companyService, times(0)).getJobOffers(anyLong());
    }
}
