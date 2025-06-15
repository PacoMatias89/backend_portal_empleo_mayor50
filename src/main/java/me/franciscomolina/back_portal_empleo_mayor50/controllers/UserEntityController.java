package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import me.franciscomolina.back_portal_empleo_mayor50.dto.UserDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.IUserService;
import me.franciscomolina.back_portal_empleo_mayor50.services.JobApplicationService;
import me.franciscomolina.back_portal_empleo_mayor50.view.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserEntityController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")  // Solo admins pueden obtener todos los usuarios
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto user) {
        try {
            userService.editClient(id, user);
            return ResponseEntity.ok("El perfil ha sido actualizado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    // Get all job applications for the authenticated ADMIN by their ID
    @GetMapping("/job-applications/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getJobApplicationsById(@PathVariable Long id) {
        try {
            List<JobApplication> jobApplications = jobApplicationService.getJobApplication(id);
            return new ResponseEntity<>(jobApplications, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/job-applications")
    @JsonView(Views.JobOfferDetail.class)
    public ResponseEntity<?> getJobApplications(@Valid @AuthenticationPrincipal UserEntityPrincipal userEntity){

        try{
            Long id = userEntity.getId();
            List<JobApplication> jobApplications = jobApplicationService.getJobApplication(id);
            return new ResponseEntity<>(jobApplications, HttpStatus.OK);
        }catch(UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        try {
            UserEntity userDelete = userService.deleteClient(id);
            return new ResponseEntity<>("Usuario " + userDelete.getName() + " ha sido eliminado", HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error inesperado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
