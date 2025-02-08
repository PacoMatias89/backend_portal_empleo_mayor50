package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.UserDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.model.Role;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobApplicationRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.UserRepository;
import me.franciscomolina.back_portal_empleo_mayor50.security.jwt.JwtProvider;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void create_Success() {
        UserDto userDto = new UserDto();
        userDto.setName("Test user");
        userDto.setLastnames("Test lastnames");
        userDto.setEmail("test@example.com");
        userDto.setBirthdate(LocalDate.parse("2021-01-01"));
        userDto.setPassword("password123");
        userDto.setConfirmPassword("password123");

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName(userDto.getName());
        user.setLastnames(userDto.getLastnames());
        user.setEmail(userDto.getEmail());
        user.setBirthdate(userDto.getBirthdate());
        user.setPassword("encodedPassword");
        user.setConfirmPassword("encodedPassword");
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDate.now());

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(passwordEncoder.encode(userDto.getConfirmPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(jwtProvider.generateTokenClient(any(UserEntity.class))).thenReturn("jwtToken");

        UserEntity createdUser = userService.create(userDto);

        assertNotNull(createdUser);
        assertEquals(userDto.getName(), createdUser.getName());
        assertEquals(userDto.getLastnames(), createdUser.getLastnames());
        assertEquals(userDto.getEmail(), createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals("encodedPassword", createdUser.getConfirmPassword());
        assertEquals("jwtToken", createdUser.getToken());

        verify(passwordEncoder, times(2)).encode(userDto.getPassword());
        verify(userRepository).save(any(UserEntity.class));
        verify(jwtProvider).generateTokenClient(any(UserEntity.class));
    }

    @Test
    void create_PasswordsDoNotMatch() {
        UserDto userDto = new UserDto();
        userDto.setPassword("password123");
        userDto.setConfirmPassword("password456");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.create(userDto));
        assertEquals("Las contraseñas no coinciden", exception.getMessage());

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
        verify(jwtProvider, never()).generateTokenClient(any());
    }

    @Test
    void findByUsername_Success() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Test user");

        when(userRepository.findByName("Test user")).thenReturn(java.util.Optional.of(user));

        java.util.Optional<UserEntity> foundUser = userService.findByUsername("Test user");

        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        assertEquals(user.getName(), foundUser.get().getName());

        verify(userRepository).findByName("Test user");
    }

    @Test
   void findByUserName_NotFound() {
       when(userRepository.findByName("Test user")).thenReturn(Optional.empty());

        Optional<UserEntity> foundUser = userService.findByUsername("Test user");

       assertTrue(foundUser.isEmpty());

       verify(userRepository).findByName("Test user");
   }

   @Test
    void findByEmail_Success() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<UserEntity> foundUser = userService.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());

        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void findByEmail_NotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.findByEmail("test@example.com"));

        assertEquals("El email no existe", exception.getMessage());
        verify(userRepository).findByEmail("test@example.com");
    }

    //TODO: Add tests for findByUsernameReturnToken, editClient, and deleteClient

    @Test
    void findByUsernameReturnToken_Success() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Test user");

        when(userRepository.findByName("Test user")).thenReturn(java.util.Optional.of(user));
        when(jwtProvider.generateTokenClient(user)).thenReturn("jwtToken");

        UserEntity foundUser = userService.findByUnernameReturnToken("Test user");

        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals("jwtToken", foundUser.getToken());

        verify(userRepository).findByName("Test user");
        verify(jwtProvider).generateTokenClient(user);
    }

    @Test
    void findByUsernameReturnToken_NotFound() {
        when(userRepository.findByName("Test user")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userService.findByUnernameReturnToken("Test user"));

        assertEquals("El usuario no fue encontrado: Test user", exception.getMessage());
        verify(userRepository).findByName("Test user");
        verify(jwtProvider, never()).generateTokenClient(any());
    }

    @Test
    void editClient_Success() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        UserDto userDto = new UserDto();
        userDto.setName("Updated name");
        userDto.setLastnames("Update lastnames");
        userDto.setEmail("updated@email.com");
        userDto.setBirthdate(LocalDate.parse("2021-01-01"));
        userDto.setPassword("updatedPassword");
        userDto.setConfirmPassword("updatedPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(passwordEncoder.encode(userDto.getConfirmPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        UserEntity updatedUser = userService.editClient(1L, userDto);

        assertNotNull(updatedUser);
        assertEquals(userDto.getName(), updatedUser.getName());
        assertEquals(userDto.getLastnames(), updatedUser.getLastnames());
        assertEquals(userDto.getEmail(), updatedUser.getEmail());
        assertEquals("encodedPassword", updatedUser.getPassword());
        assertEquals("encodedPassword", updatedUser.getConfirmPassword());

        verify(userRepository).findById(1L); //Encontramos al usuario por ID
        verify(passwordEncoder, times(2)).encode(userDto.getPassword()); //Encriptamos la contraseña
        verify(userRepository).save(user); //Guardamos los cambios
    }

    @Test
    void editClient_UserNotFound() {
        UserDto userDto = new UserDto();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.editClient(1L, userDto);
        });

        assertEquals("El usuario no fue encontrado: 1", exception.getMessage());
        verify(userRepository).findById(1L);
    }


    @Test
    void deleteClient_Success(){
        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        UserEntity deletedUser = userService.deleteClient(1L);

        assertNotNull(deletedUser);
        assertEquals(user.getId(), deletedUser.getId());

        verify(userRepository).findById(1L);
        verify(userRepository).deleteById(1L);

    }

    @Test
    void deleteClient_UserNotFound(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.deleteClient(1L);
        });

        assertEquals("El usuario no fue encontrado: 1", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).deleteById(1L);
    }

    @Test
    void getJobApplications_Success(){
        UserEntity user = new UserEntity();
        user.setId(1L);

        JobApplication jobApplication = new JobApplication();
        jobApplication.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(jobApplicationRepository.findByUser(user)).thenReturn(List.of(jobApplication));

        List<JobApplication> jobApplications = userService.getJobApplications(1L);

        assertNotNull(jobApplications);
        assertEquals(1, jobApplications.size());
        assertEquals(1L, jobApplications.get(0).getId());

        verify(userRepository).findById(1L);
        verify(jobApplicationRepository).findByUser(user);

    }

    @Test
    void getJobApplications_UserNotFound(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.getJobApplications(1L);
        });

        assertEquals("El usuario no fue encontrado: 1", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(jobApplicationRepository, never()).findByUser(any());
    }




}