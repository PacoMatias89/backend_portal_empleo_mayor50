package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import me.franciscomolina.back_portal_empleo_mayor50.dto.UserDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.IUserService;
import me.franciscomolina.back_portal_empleo_mayor50.services.JobApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        // Given
        UserEntityPrincipal userEntity = Mockito.mock(UserEntityPrincipal.class);
        UserDto user = new UserDto();
        Long userId = 1L;

        // Simula el comportamiento de obtener el ID del usuario
        when(userEntity.getId()).thenReturn(userId);

        // When
        ResponseEntity<?> response = userEntityController.updataUser(userEntity, user);

        // Then
        verify(userService).editClient(userId, user);  // Verifica que el método editClient fue llamado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("El perfil ha sido actualizado correctamente", response.getBody());
    }

    @Test
    void updateUser_Fail() {
        // Given
        UserEntityPrincipal userEntity = Mockito.mock(UserEntityPrincipal.class);
        UserDto user = new UserDto();
        Long userId = 1L;

        // Simula el comportamiento de obtener el ID del usuario
        when(userEntity.getId()).thenReturn(userId);

        // Simula el comportamiento de lanzar una excepción
        doThrow(new UsernameNotFoundException("User not found")).when(userService).editClient(userId, user);

        // When
        ResponseEntity<?> response = userEntityController.updataUser(userEntity, user);

        // Then
        verify(userService).editClient(userId, user);  // Verifica que el método editClient fue llamado
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void getJobApplications_Success() {
        // Given
        UserEntityPrincipal userEntity = Mockito.mock(UserEntityPrincipal.class);
        Long userId = 1L;

        // Simula el comportamiento de obtener el ID del usuario
        when(userEntity.getId()).thenReturn(userId);

        // When
        ResponseEntity<?> response = userEntityController.getJobApplications(userEntity);

        // Then
        verify(jobApplicationService).getJobApplication(userId);  // Verifica que el método getJobApplication fue llamado
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getJobApplications_Fail() {
        // Given
        UserEntityPrincipal userEntity = Mockito.mock(UserEntityPrincipal.class);
        Long userId = 1L;

        // Simula el comportamiento de obtener el ID del usuario
        when(userEntity.getId()).thenReturn(userId);

        // Simula el comportamiento de lanzar una excepción
        doThrow(new UsernameNotFoundException("User not found")).when(jobApplicationService).getJobApplication(userId);

        // When
        ResponseEntity<?> response = userEntityController.getJobApplications(userEntity);

        // Then
        verify(jobApplicationService).getJobApplication(userId);  // Verifica que el método getJobApplication fue llamado
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }



    @Test
    void deleteUser_Success() {
        // Given
        UserEntityPrincipal userEntity = Mockito.mock(UserEntityPrincipal.class);
        Long userId = 1L;
        UserEntity deletedUser = new UserEntity(); // Simula el usuario eliminado

        // Simula el comportamiento de obtener el ID del usuario
        when(userEntity.getId()).thenReturn(userId);

        // Simula el comportamiento de eliminar el usuario
        when(userService.deleteClient(userId)).thenReturn(deletedUser);

        // When
        ResponseEntity<String> response = userEntityController.deleteUser(userEntity);

        // Then
        verify(userService).deleteClient(userId);  // Verifica que el método deleteClient fue llamado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("El usuario ha sido eliminado correctamente", response.getBody());
    }


    @Test
    void deleteUser_Fail() {
        // Given
        UserEntityPrincipal userEntity = Mockito.mock(UserEntityPrincipal.class);
        Long userId = 1L;

        // Simula el comportamiento de obtener el ID del usuario
        when(userEntity.getId()).thenReturn(userId);

        // Simula el comportamiento de lanzar una excepción
        when(userService.deleteClient(userId)).thenThrow(new RuntimeException("Error inesperado"));

        // When
        ResponseEntity<String> response = userEntityController.deleteUser(userEntity);

        // Then
        verify(userService).deleteClient(userId);  // Verifica que el método deleteClient fue llamado
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error inesperado: Error inesperado", response.getBody());
    }


}
