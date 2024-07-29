package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.CompanyDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.model.Role;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.CompanyRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobOfferRepository;
import me.franciscomolina.back_portal_empleo_mayor50.security.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CompanyServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private JobOfferRepository jobOfferRepository;

    @InjectMocks
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCompany_Success(){
        CompanyDto companyDto = new CompanyDto();

        // Data to access the portal
        companyDto.setName("name");
        companyDto.setLastname("lastname");
        companyDto.setEmail("email");
        companyDto.setPassword("password");
        companyDto.setConfirmPasswordCompany("password");

        // Data of the company
        companyDto.setCompanyName("companyName");
        companyDto.setPhoneContact("phoneContact");
        companyDto.setCifCompany("B12345678");
        companyDto.setIsEtt(true);
        companyDto.setDescription("description");

        // Create a Company object to return when save is called
        Company expectedCompany = new Company();
        expectedCompany.setId(1L);
        expectedCompany.setName(companyDto.getName());
        expectedCompany.setLastname(companyDto.getLastname());
        expectedCompany.setEmail(companyDto.getEmail());
        expectedCompany.setPassword("encodedPassword");
        expectedCompany.setConfirmPasswordCompany("encodedPassword");
        expectedCompany.setCompanyName(companyDto.getCompanyName());
        expectedCompany.setPhoneContact(companyDto.getPhoneContact());
        expectedCompany.setCifCompany(companyDto.getCifCompany());
        expectedCompany.setIsEtt(companyDto.getIsEtt());
        expectedCompany.setDescription(companyDto.getDescription());
        expectedCompany.setRole(Role.COMPANY);

        // Mock the passwordEncoder to return a fixed value
        when(passwordEncoder.encode(companyDto.getPassword())).thenReturn("encodedPassword");
        when(passwordEncoder.encode(companyDto.getConfirmPasswordCompany())).thenReturn("encodedPassword");

        // Mock the companyRepository to return the expectedCompany when save is called
        when(companyRepository.save(any(Company.class))).thenReturn(expectedCompany);

        // Mock the jwtProvider to return a fixed value
        String jwtToken = "jwtToken";
        when(jwtProvider.generateTokenCompany(expectedCompany)).thenReturn(jwtToken);

        Company company = companyService.create(companyDto);

        // Data to access the portal
        assertEquals(companyDto.getName(), company.getName());
        assertEquals(companyDto.getLastname(), company.getLastname());
        assertEquals(companyDto.getEmail(), company.getEmail());
        assertEquals("encodedPassword", company.getPassword());
        assertEquals("encodedPassword", company.getConfirmPasswordCompany());

        // Data of the company
        assertEquals(companyDto.getCompanyName(), company.getCompanyName());
        assertEquals(companyDto.getPhoneContact(), company.getPhoneContact());
        assertEquals(companyDto.getCifCompany(), company.getCifCompany());
        assertEquals(companyDto.getIsEtt(), company.getIsEtt());
        assertEquals(companyDto.getDescription(), company.getDescription());
        assertEquals(Role.COMPANY, company.getRole());

        // Verify the token was set
        assertEquals(jwtToken, company.getToken());

        // Verify the repository save method was called
        verify(companyRepository).save(any(Company.class));



    }

    @Test
    void createCompany_PasswordsDoNotMatch(){
        CompanyDto companyDto = new CompanyDto();

        // Data to access the portal
        companyDto.setName("name");
        companyDto.setLastname("lastname");
        companyDto.setEmail("email");
        companyDto.setPassword("password");
        companyDto.setConfirmPasswordCompany("password2");

        // Data of the company
        companyDto.setCompanyName("companyName");
        companyDto.setPhoneContact("phoneContact");
        companyDto.setCifCompany("B12345678");
        companyDto.setIsEtt(true);
        companyDto.setDescription("description");

        // Mock the passwordEncoder to return a fixed value
        when(passwordEncoder.encode(companyDto.getPassword())).thenReturn("encodedPassword");
        when(passwordEncoder.encode(companyDto.getConfirmPasswordCompany())).thenReturn("encodedPassword");

        // Verify that an exception is thrown when the passwords do not match
        assertThrows(IllegalArgumentException.class, () -> companyService.create(companyDto));

        // Verify the repository save method was not called
        verify(companyRepository, never()).save(any(Company.class));
    }


    @Test
    void findByUsername_Success(){
        String name = "name";
        Company company = new Company();
        when(companyRepository.findByName(name)).thenReturn(Optional.of(company));

        assertEquals(Optional.of(company), companyService.findByUsername(name));

        verify(companyRepository).findByName(name);
    }

    @Test
    void findByUsername_NotFound(){
        String name = "name2";
        when(companyRepository.findByName(name)).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), companyService.findByUsername(name));

        verify(companyRepository).findByName(name);
    }

    @Test
    void findByEmail_Success(){
        String emailCompany = "emailCompany@gmail.com";
        Company company = mock(Company.class);
        when(companyRepository.findByEmail(emailCompany)).thenReturn(Optional.of(company));

        assertEquals(Optional.of(company), companyService.findByEmail(emailCompany));

        verify(companyRepository).findByEmail(emailCompany);
    }


    @Test
    void findByEmail_NotFound() {
        String emailCompany = "testCompany3@gmail.com";
        when(companyRepository.findByEmail(emailCompany)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> companyService.findByEmail(emailCompany));
        assertEquals("El email no existe", exception.getMessage());

        verify(companyRepository).findByEmail(emailCompany);
    }

    @Test
    void findByNameReturnToken_Success(){
        String name = "companyName";
        Company company = new Company();
        company.setName(name);
        company.setId(1L); // Establecer ID u otros valores necesarios para que la generación del token sea válida

        // Mockear el repositorio para que devuelva la compañía
        when(companyRepository.findByName(name)).thenReturn(Optional.of(company));

        // Mockear el JwtProvider para que devuelva un token
        String token = "generatedJwtToken";
        when(jwtProvider.generateTokenCompany(company)).thenReturn(token);

        // Llamar al método a probar
        Company result = companyService.findByNameReturnToken(name);

        // Verificar los resultados
        assertEquals(company, result);
        assertEquals(token, result.getToken());

        // Verificar que se llamaron los métodos esperados
        verify(companyRepository).findByName(name);
        verify(jwtProvider).generateTokenCompany(company);
    }

    @Test
    void findByNameReturnToken_NotFound(){
        String name = "name2";

        //Mockear el repositorio para que devuelva un Optional vacío
        when(companyRepository.findByName(name)).thenReturn(Optional.empty());

        //Verificar que se lanza una excepción cuando no se encuentra la compañía
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> companyService.findByNameReturnToken(name));
    }

    @Test
    void editCompany_Success(){
        Long id = 1L;
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("newName");
        companyDto.setLastname("newLastname");
        companyDto.setEmail("newEmail");
        companyDto.setPassword("newPassword");
        companyDto.setConfirmPasswordCompany("newPassword");
        companyDto.setCompanyName("newCompanyName");
        companyDto.setPhoneContact("newPhoneContact");
        companyDto.setCifCompany("newCifCompany");
        companyDto.setIsEtt(true);
        companyDto.setDescription("newDescription");

        Company existingCompany = new Company();
        existingCompany.setId(id);
        existingCompany.setName("oldName");
        existingCompany.setLastname("oldLastname");
        existingCompany.setEmail("oldEmail");
        existingCompany.setPassword("oldPassword");
        existingCompany.setConfirmPasswordCompany("oldPassword");
        existingCompany.setCompanyName("oldCompanyName");
        existingCompany.setPhoneContact("oldPhoneContact");
        existingCompany.setCifCompany("oldCifCompany");
        existingCompany.setIsEtt(false);
        existingCompany.setDescription("oldDescription");

        // Mock the companyRepository to return the existing company when findById is called
        when(companyRepository.findById(id)).thenReturn(Optional.of(existingCompany));

        // Mock the passwordEncoder to return the encoded password
        when(passwordEncoder.encode(companyDto.getPassword())).thenReturn("encodedPassword");
        when(passwordEncoder.encode(companyDto.getConfirmPasswordCompany())).thenReturn("encodedPassword");

        // Mock the companyRepository to return the updated company when save is called
        when(companyRepository.save(any(Company.class))).thenReturn(existingCompany);

        Company result = companyService.editCompany(id, companyDto);

        // Verify the updated fields
        assertEquals("newName", result.getName());
        assertEquals("newLastname", result.getLastname());
        assertEquals("newEmail", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("encodedPassword", result.getConfirmPasswordCompany());
        assertEquals("newCompanyName", result.getCompanyName());
        assertEquals("newPhoneContact", result.getPhoneContact());
        assertEquals("newCifCompany", result.getCifCompany());
        assertTrue(result.getIsEtt());
        assertEquals("newDescription", result.getDescription());

        // Verify the repository interactions
        verify(companyRepository).findById(id);
        verify(companyRepository).save(existingCompany);
    }

    @Test
    void editCompany_CompanyNotFound(){
        Long id = 1L;
        CompanyDto companyDto = new CompanyDto();

        // Mock the companyRepository to return an empty Optional when findById is called
        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        // Verify that an exception is thrown when the company is not found
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> companyService.editCompany(id, companyDto));
        assertEquals("La compañía no fue encontrado:" + id, exception.getMessage());

        // Verify the repository save method was not called
        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    void deleteCompany_Success(){
        Long id = 1L;
        Company company = new Company();
        company.setId(id);

        // Mock the companyRepository to return the company when findById is called
        when(companyRepository.findById(id)).thenReturn(Optional.of(company));

        Company result = companyService.deleteCompany(id);

        // Verify the repository interactions
        verify(companyRepository).findById(id);
        verify(companyRepository).delete(company);

        // Verify the result
        assertEquals(company, result);
    }


    @Test
    void deleteCompany_CompanyNotFound(){
        Long id = 1L;

        // Mock the companyRepository to return an empty Optional when findById is called
        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        // Verify that an exception is thrown when the company is not found
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> companyService.deleteCompany(id));
        assertEquals("La compañía no fue encontrado:" + id, exception.getMessage());

        // Verify the repository delete method was not called
        verify(companyRepository, never()).delete(any(Company.class));
    }


    @Test
    void getJobOffers_Success(){
        Long id = 1L;
        Company company = new Company();
        company.setId(id);
       List<JobOffer> jobOffers = List.of(new JobOffer(), new JobOffer());

        // Mock the companyRepository to return the company when findById is called
        when(companyRepository.findById(id)).thenReturn(Optional.of(company));

        // Mock the jobOfferRepository to return the jobOffers when findByCompany is called
        when(jobOfferRepository.findByCompany(company)).thenReturn(jobOffers);

        List<JobOffer> result = companyService.getJobOffers(id);

        // Verify the repository interactions
        verify(companyRepository).findById(id);
        verify(jobOfferRepository).findByCompany(company);

        // Verify the result
        assertEquals(jobOffers, result);
    }
}