package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import jakarta.validation.Valid;
import me.franciscomolina.back_portal_empleo_mayor50.dto.WorkExperienceDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.WorkExperience;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.IWorkExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/work-experience")
public class WorkExperienceController {

    @Autowired
    private IWorkExperienceService workExperienceService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createWorkExperience(@Valid @AuthenticationPrincipal UserEntityPrincipal userEntityPrincipal, @RequestBody @Valid WorkExperienceDto workExperienceDTO) {
        try {
            Long userId = userEntityPrincipal.getId();
            workExperienceDTO.setUserId(userId);
            workExperienceService.saveWorkExperience(workExperienceDTO);
            return new ResponseEntity<>("La experiencia laboral ha sido creada correctamente", HttpStatus.CREATED);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_work-experiences")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getWorkExperiences(@RequestParam(required = false) Long userId, @AuthenticationPrincipal Object principal) {
        Map<String, Object> response = new HashMap<>();

        // Verificar si el principal es un Usuario o una Compañía
        if (principal instanceof UserEntityPrincipal) {
            UserEntityPrincipal userEntityPrincipal = (UserEntityPrincipal) principal;
            Long currentUserId = userEntityPrincipal.getId();
            List<WorkExperience> workExperiences = workExperienceService.getuserWorkExperience(currentUserId);

            // Calculamos la experiencia laboral y la agregamos al final del JSON
            workExperiences.forEach(exp -> exp.setExperience(exp.getExperience()));
            String totalExperience = workExperienceService.calculateTotalExperience(workExperiences);

            response.put("workExperiences", workExperiences);
            response.put("totalExperience", totalExperience);

        } else if (principal instanceof CompanyEntityPrincipal) {
            // Verificar si la empresa proporcionó un userId para acceder a las experiencias
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Debe proporcionar un userId válido para ver las experiencias laborales"));
            }

            CompanyEntityPrincipal companyEntityPrincipal = (CompanyEntityPrincipal) principal;
            Long companyId = companyEntityPrincipal.getId();
            // Obtener las experiencias laborales del usuario con el userId proporcionado
            List<WorkExperience> companyWorkExperiences = workExperienceService.getuserWorkExperience(userId);

            response.put("companyWorkExperiences", companyWorkExperiences);
        } else {
            // Si el principal no es ni un usuario ni una compañía, devolver error con un Map vacío
            response.put("error", "Acceso no autorizado.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get_work-experiences/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWorkExperienceById(@PathVariable Long id) {
        try {
            WorkExperience workExperience = workExperienceService.getWorkExperienceById(id).orElseThrow();
            return new ResponseEntity<>(workExperience, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No se encontró la experiencia laboral con el id: " + id);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateWorkExperience(@PathVariable Long id, @RequestBody @Valid WorkExperienceDto workExperienceDto, @AuthenticationPrincipal UserEntityPrincipal userEntityPrincipal) {
        try {
            Long userId = userEntityPrincipal.getId();
            workExperienceDto.setUserId(userId);
            workExperienceService.updateWorkExperience(id, workExperienceDto);
            return new ResponseEntity<>("La experiencia laboral ha sido actualizada correctamente", HttpStatus.OK);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteWorkExperience(@PathVariable Long id, @AuthenticationPrincipal UserEntityPrincipal userEntityPrincipal) {
        try {
            workExperienceService.deleteWorkExperience(id);
            return new ResponseEntity<>("La experiencia laboral ha sido eliminada correctamente", HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No se encontró la experiencia laboral con el id: " + id);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
}
