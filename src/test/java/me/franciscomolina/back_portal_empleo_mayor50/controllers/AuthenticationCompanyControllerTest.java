package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import me.franciscomolina.back_portal_empleo_mayor50.dto.CompanyDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.services.IAuthenticationServiceCompany;
import me.franciscomolina.back_portal_empleo_mayor50.services.ICompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@SpringBootTest
class AuthenticationCompanyControllerTest {

    @Mock
    private IAuthenticationServiceCompany serviceCompany;

    @Mock
    private ICompanyService companyService;

    @InjectMocks
    private AuthenticationCompanyController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_Success() {
        // Arrange
        CompanyDto companyDto = new CompanyDto();
        Company company = new Company();
        when(companyService.create(any(CompanyDto.class))).thenReturn(company);

        // Act
        ResponseEntity<?> response = controller.signUp(companyDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(company, response.getBody());
        verify(companyService, times(1)).create(any(CompanyDto.class));
    }

    @Test
    void signUp_Conflict() {
        // Arrange
        CompanyDto companyDto = new CompanyDto();
        when(companyService.create(any(CompanyDto.class))).thenReturn(null);

        // Act
        ResponseEntity<?> response = controller.signUp(companyDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(companyService, times(1)).create(any(CompanyDto.class));
    }

    @Test
    void signUp_BadRequest() {
        // Arrange
        CompanyDto companyDto = new CompanyDto();
        when(companyService.create(any(CompanyDto.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        // Act
        ResponseEntity<?> response = controller.signUp(companyDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data", response.getBody());
        verify(companyService, times(1)).create(any(CompanyDto.class));
    }

    @Test
    void signIn_Success() {
        // Arrange
        Company company = new Company();
        Company signedInCompany = new Company();
        signedInCompany.setToken("jwt-token");
        when(serviceCompany.signInAndReturnJWTCompany(any(Company.class))).thenReturn(signedInCompany);

        // Act
        ResponseEntity<?> response = controller.signIn(company);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(signedInCompany, response.getBody());
        verify(serviceCompany, times(1)).signInAndReturnJWTCompany(any(Company.class));
    }

    @Test
    void signIn_BadRequest() {
        // Arrange
        Company company = new Company();
        when(serviceCompany.signInAndReturnJWTCompany(any(Company.class))).thenThrow(new IllegalArgumentException("Invalid credentials"));

        // Act
        ResponseEntity<?> response = controller.signIn(company);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
        verify(serviceCompany, times(1)).signInAndReturnJWTCompany(any(Company.class));
    }
}
