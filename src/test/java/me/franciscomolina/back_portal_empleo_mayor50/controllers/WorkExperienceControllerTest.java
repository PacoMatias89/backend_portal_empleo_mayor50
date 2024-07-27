package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import me.franciscomolina.back_portal_empleo_mayor50.dto.WorkExperienceDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.WorkExperience;
import me.franciscomolina.back_portal_empleo_mayor50.services.IWorkExperienceService;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class WorkExperienceControllerTest {

    @InjectMocks
    private WorkExperienceController workExperienceController;

    @Mock
    private IWorkExperienceService workExperienceService;

    @Mock
    private UserEntityPrincipal userEntityPrincipal;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void updateWorkExperience_Success() {
        Long id = 1L;
        WorkExperienceDto dto = new WorkExperienceDto();
        dto.setCompanyName("New Company");
        dto.setPosition("New Position");
        dto.setStartDate(LocalDate.now().minusMonths(6));
        dto.setEndDate(LocalDate.now());
        dto.setUserId(1L);

        WorkExperience updatedExperience = new WorkExperience();
        updatedExperience.setId(id);

        when(userEntityPrincipal.getId()).thenReturn(1L);
        when(workExperienceService.updateWorkExperience(eq(id), any(WorkExperienceDto.class)))
                .thenReturn(updatedExperience);

        ResponseEntity<?> response = workExperienceController.updateWorkExperience(id, dto, userEntityPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("La experiencia laboral ha sido actualizada correctamente", response.getBody());
    }

    @Test
    public void updateWorkExperience_NotFound() {
        Long id = 1L;
        WorkExperienceDto dto = new WorkExperienceDto();
        dto.setCompanyName("New Company");
        dto.setPosition("New Position");
        dto.setStartDate(LocalDate.now().minusMonths(6));
        dto.setEndDate(LocalDate.now());
        dto.setUserId(1L);

        when(userEntityPrincipal.getId()).thenReturn(1L);
        when(workExperienceService.updateWorkExperience(eq(id), any(WorkExperienceDto.class)))
                .thenThrow(new IllegalArgumentException("No se encontró la experiencia laboral con el id: " + id));

        ResponseEntity<?> response = workExperienceController.updateWorkExperience(id, dto, userEntityPrincipal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertNotNull(body);
        assertEquals("No se encontró la experiencia laboral con el id: " + id, body.get("error"));
    }



    @Test
    public void deleteWorkExperience_Success() {
        Long id = 1L;
        WorkExperience workExperience = mock(WorkExperience.class);



        when(workExperienceService.deleteWorkExperience(id)).thenReturn(workExperience);

        ResponseEntity<?> response = workExperienceController.deleteWorkExperience(id, userEntityPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("La experiencia laboral ha sido eliminada correctamente", response.getBody());
    }

    @Test
    public void deleteWorkExperience_NotFound() {
        Long id = 1L;

        when(workExperienceService.deleteWorkExperience(id))
                .thenThrow(new RuntimeException("No se encontró la experiencia laboral con el id: " + id));

        ResponseEntity<?> response = workExperienceController.deleteWorkExperience(id, userEntityPrincipal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertNotNull(body);
        assertEquals("No se encontró la experiencia laboral con el id: " + id, body.get("error"));
    }

}
