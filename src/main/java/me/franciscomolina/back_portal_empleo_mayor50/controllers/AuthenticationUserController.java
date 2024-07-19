package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import me.franciscomolina.back_portal_empleo_mayor50.dto.UserDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.services.IAuthenticationService;
import me.franciscomolina.back_portal_empleo_mayor50.services.IUserService;
import me.franciscomolina.back_portal_empleo_mayor50.view.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationUserController {

    @Autowired
    private IAuthenticationService authenticationService;

    @Autowired
    private IUserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> singUp(@Valid @RequestBody UserDto userDto) {
        try {
            UserEntity client = userService.create(userDto);
            if (client == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(client, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/sign-in")
    @JsonView(Views.UserSignInView.class) // Aplicar la vista al endpoint
    public ResponseEntity<?> signIn(@RequestBody UserEntity client) {
        try {
            return new ResponseEntity<>(authenticationService.signInAndReturnJWT(client), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
        }
    }
}
