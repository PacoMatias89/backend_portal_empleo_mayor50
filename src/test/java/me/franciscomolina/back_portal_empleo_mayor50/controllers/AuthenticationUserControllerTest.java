package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import me.franciscomolina.back_portal_empleo_mayor50.dto.UserDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.services.IAuthenticationService;
import me.franciscomolina.back_portal_empleo_mayor50.services.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationUserControllerTest {

    @Mock
    private IAuthenticationService authenticationService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private AuthenticationUserController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void singUp_Success() {
        // Arrange
        UserDto userDto = new UserDto();
        UserEntity client = new UserEntity();
        when(userService.create(any(UserDto.class))).thenReturn(client);

        // Act
        ResponseEntity<?> response = controller.singUp(userDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(client, response.getBody());
        verify(userService, times(1)).create(any(UserDto.class));
    }

    @Test
    void singUp_Conflict() {
        // Arrange
        UserDto userDto = new UserDto();
        when(userService.create(any(UserDto.class))).thenReturn(null);

        // Act
        ResponseEntity<?> response = controller.singUp(userDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(userService, times(1)).create(any(UserDto.class));
    }

    @Test
    void signUp_BadRequest() {
        // Arrange
        UserDto userDto = new UserDto();
        when(userService.create(any(UserDto.class))).thenThrow(new IllegalArgumentException("Bad request"));

        // Act
        ResponseEntity<?> response = controller.singUp(userDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad request", response.getBody());
        verify(userService, times(1)).create(any(UserDto.class));
    }

    @Test
    void signIn_Success() {
        // Arrange
        UserEntity client = new UserEntity();
        UserEntity signedInClient = new UserEntity();
        when(authenticationService.signInAndReturnJWT(client)).thenReturn(signedInClient);

        // Act
        ResponseEntity<?> response = controller.signIn(client);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(signedInClient, response.getBody());
        verify(authenticationService, times(1)).signInAndReturnJWT(client);
    }


    @Test
    void signIn_BadRequest() {
        // Arrange
        UserEntity client = new UserEntity();
        when(authenticationService.signInAndReturnJWT(client)).thenThrow(new IllegalArgumentException("Bad request"));

        // Act
        ResponseEntity<?> response = controller.signIn(client);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ERROR: Bad request", response.getBody());
        verify(authenticationService, times(1)).signInAndReturnJWT(client);
    }

}