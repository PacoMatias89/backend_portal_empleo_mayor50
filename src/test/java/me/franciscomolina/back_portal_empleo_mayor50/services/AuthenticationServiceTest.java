package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.UserRepository;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.security.jwt.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserRepository userRepository;



    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void signInAndReturnJWT_Success() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setName("Test User");

        UserEntityPrincipal userPrincipal = new UserEntityPrincipal();
        userPrincipal.setUser(user);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(jwtProvider.generateToken(userPrincipal)).thenReturn("jwt-token");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act
        UserEntity result = authenticationService.signInAndReturnJWT(user);

        // Assert
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider, times(1)).generateToken(userPrincipal);
    }


    @Test
    public void signInAndReturnJWT_UserNotFound() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.signInAndReturnJWT(user));
    }
}
