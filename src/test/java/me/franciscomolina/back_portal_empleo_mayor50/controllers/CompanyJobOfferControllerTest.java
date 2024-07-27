package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import me.franciscomolina.back_portal_empleo_mayor50.dto.JobOfferDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.IJobOfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
class CompanyJobOfferControllerTest {

    @Mock
    private IJobOfferService jobOfferService;

    @InjectMocks
    private CompanyJobOfferController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createJobOffer_Success() {
        CompanyEntityPrincipal mockPrincipal = mock(CompanyEntityPrincipal.class);
        when(mockPrincipal.getId()).thenReturn(1L);

        JobOfferDto jobOfferDto = new JobOfferDto();

        ResponseEntity<?> response = controller.createJobOffer(mockPrincipal, jobOfferDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Oferta de trabajo creada correctamente", response.getBody());
        verify(jobOfferService, times(1)).createJobOffer(jobOfferDto, 1L);
    }

    @Test
    void createJobOffer_NullPrincipal(){
        CompanyEntityPrincipal mockPrincipal = mock(CompanyEntityPrincipal.class);
        when(mockPrincipal.getId()).thenReturn(1L);
        JobOfferDto jobOfferDto = new JobOfferDto();

        //Lanzamos la excepción
        doThrow(new RuntimeException("Principal is null")).when(jobOfferService).createJobOffer(jobOfferDto, 1L);

        ResponseEntity<?> response = controller.createJobOffer(mockPrincipal, jobOfferDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Principal is null", response.getBody());
    }

    @Test
    void getJobOffers_Success() {
        when(jobOfferService.getJobOffers()).thenReturn(List.of(new JobOffer()));

        ResponseEntity<?> response = controller.getJobOffers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void updateJobOffer_Success(){
        CompanyEntityPrincipal mockPrincipal = mock(CompanyEntityPrincipal.class);
        when(mockPrincipal.getId()).thenReturn(1L);

        JobOfferDto jobOfferDto = new JobOfferDto();

        ResponseEntity<?> response = controller.updateJobOffer(mockPrincipal, jobOfferDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Oferta de trabajo actualizada correctamente", response.getBody());
        verify(jobOfferService, times(1)).updateJobOffer(1L, jobOfferDto);
    }

    @Test
    void updateJobOffer_Failure(){
        CompanyEntityPrincipal mockPrincipal = mock(CompanyEntityPrincipal.class);
        when(mockPrincipal.getId()).thenReturn(1L);
        JobOfferDto jobOfferDto = new JobOfferDto();

        //Lanzamos la excepción
        doThrow(new RuntimeException("Job offer update failed")).when(jobOfferService).updateJobOffer(1L, jobOfferDto);

        ResponseEntity<?> response = controller.updateJobOffer(mockPrincipal, jobOfferDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Job offer update failed", response.getBody());
    }


    @Test
    void deleteJobOffer_Success(){
        CompanyEntityPrincipal mockPrincipal = mock(CompanyEntityPrincipal.class);
        when(mockPrincipal.getId()).thenReturn(1L);

        ResponseEntity<?> response = controller.deleteJobOffer(mockPrincipal, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Oferta de trabajo eliminada correctamente", response.getBody());
        verify(jobOfferService, times(1)).deleteJobOffer(1L);
    }

    @Test
    void deleteJobOffer_Failure(){
        CompanyEntityPrincipal mockPrincipal = mock(CompanyEntityPrincipal.class);
        when(mockPrincipal.getId()).thenReturn(1L);

        //Lanzamos la excepción
        doThrow(new RuntimeException("Job offer deletion failed")).when(jobOfferService).deleteJobOffer(1L);

        ResponseEntity<?> response = controller.deleteJobOffer(mockPrincipal, 1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Job offer deletion failed", response.getBody());
    }





}