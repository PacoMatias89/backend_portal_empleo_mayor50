package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.CompanyRepository;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.security.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceCompanyTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private AuthenticationServiceCompany authenticationServiceCompany;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signInAndReturnJWTCompany_Success() {
        // Arrange
        Company company = new Company();
        company.setEmail("test@example.com");
        company.setPassword("password123");
        company.setName("Test Company");

        CompanyEntityPrincipal companyPrincipal = new CompanyEntityPrincipal();
        companyPrincipal.setCompany(company);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(companyPrincipal);
        when(jwtProvider.generateTokenToCompany(companyPrincipal)).thenReturn("jwt-token");
        when(companyRepository.findByEmail(company.getEmail())).thenReturn(Optional.of(company));

        // Act
        Company result = authenticationServiceCompany.signInAndReturnJWTCompany(company);

        // Assert
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider, times(1)).generateTokenToCompany(companyPrincipal);
        verify(companyRepository, times(1)).findByEmail(company.getEmail());
    }

    @Test
    void signInAndReturnJWTCompany_CompanynotFound() {
        // Arrange
        Company company = new Company();
        company.setEmail("test@example.com");
        company.setPassword("password123");

        when(companyRepository.findByEmail(company.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authenticationServiceCompany.signInAndReturnJWTCompany(company));
    }


}
