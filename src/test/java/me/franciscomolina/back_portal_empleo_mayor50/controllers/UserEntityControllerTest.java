package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import me.franciscomolina.back_portal_empleo_mayor50.dto.UserDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.services.IUserService;
import me.franciscomolina.back_portal_empleo_mayor50.services.JobApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserEntityControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private JobApplicationService jobApplicationService;

    @InjectMocks
    private UserEntityController userEntityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateUser_Success() {
        Long userId = 1L;
        UserDto user = new UserDto();

        ResponseEntity<?> response = userEntityController.updateUser(userId, user);

        verify(userService).editClient(userId, user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("El perfil ha sido actualizado correctamente", response.getBody());
    }

    @Test
    void updateUser_Fail() {
        Long userId = 1L;
        UserDto user = new UserDto();

        doThrow(new UsernameNotFoundException("User not found")).when(userService).editClient(userId, user);

        ResponseEntity<?> response = userEntityController.updateUser(userId, user);

        verify(userService).editClient(userId, user);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }
}
