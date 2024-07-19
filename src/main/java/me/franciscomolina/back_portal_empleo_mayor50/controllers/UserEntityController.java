package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationDTO;
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

    @PutMapping("/update")
    public ResponseEntity<?> updataUser(@Valid @AuthenticationPrincipal UserEntityPrincipal userEntity, @RequestBody UserDto user){

        try{
            Long id = userEntity.getId();
            userService.editClient(id, user);
            return new ResponseEntity<>( "El perfil ha sido actualizado correctamente", HttpStatus.OK);
        }catch(UsernameNotFoundException e){
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
    public ResponseEntity<?> deleteUser(@Valid @AuthenticationPrincipal UserEntityPrincipal userEntity){

        try{
            Long id = userEntity.getId();
            UserEntity userDelete = userService.deleteClient(id);
            return new ResponseEntity<>("Usuario " + userDelete.getName() + " ha sido eliminado", HttpStatus.OK);
        }catch(UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
