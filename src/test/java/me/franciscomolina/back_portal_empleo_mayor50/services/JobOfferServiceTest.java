package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.JobOfferDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.CompanyRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobApplicationRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobOfferRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class JobOfferServiceTest {

    @Mock
    private JobOfferRepository jobOfferRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private JobOfferService jobOfferService;

    @Test

    void createJobOffer_Success(){
        //Primero buscamos la empresa
        Company company = new Company();
        company.setId(1L);
        company.setName("Empresa de prueba");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        //Creamos la oferta dto de trabajo
        JobOfferDto jobOfferDto = new JobOfferDto();
        jobOfferDto.setTitle("Oferta de prueba");
        jobOfferDto.setDescription("Descripción de la oferta de prueba");
        jobOfferDto.setSalary(20000.00);
        jobOfferDto.setRequirements("Requisitos de la oferta de prueba");
        jobOfferDto.setLocation("Málaga");
        jobOfferDto.setCreatedAt(LocalDate.now());

        //Creamos la oferta de trabajo
        JobOffer jobOffer = new JobOffer();
        jobOffer.setTitle(jobOfferDto.getTitle());
        jobOffer.setDescription(jobOfferDto.getDescription());
        jobOffer.setSalary(jobOfferDto.getSalary());
        jobOffer.setRequirements(jobOfferDto.getRequirements());
        jobOffer.setLocation(jobOfferDto.getLocation());
        jobOffer.setCreatedAt(LocalDate.now());

        //Configuramos los mock
        when(jobOfferRepository.save(any(JobOffer.class))).thenReturn(jobOffer);

        //Llamamos al método a testear
        JobOffer createdJobOffer = jobOfferService.createJobOffer(jobOfferDto, 1L);

        //Comprobamos que la oferta de trabajo se ha creado correctamente
        assertNotNull(createdJobOffer);
        assertEquals(jobOfferDto.getTitle(), createdJobOffer.getTitle());
        assertEquals(jobOfferDto.getDescription(), createdJobOffer.getDescription());
        assertEquals(jobOfferDto.getSalary(), createdJobOffer.getSalary());
        assertEquals(jobOfferDto.getRequirements(), createdJobOffer.getRequirements());
        assertEquals(jobOfferDto.getLocation(), createdJobOffer.getLocation());
        assertEquals(jobOfferDto.getCreatedAt(), createdJobOffer.getCreatedAt());

        verify(jobOfferRepository).save(any(JobOffer.class));
        verify(companyRepository).findById(1L);


    }


    @Test
    void createJobOffer_CompanyNotFound(){
        //Configuramos el mock para que devuelva un Optional vacío
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        //Creamos la oferta dto de trabajo
        JobOfferDto jobOfferDto = new JobOfferDto();
        jobOfferDto.setTitle("Oferta de prueba");
        jobOfferDto.setDescription("Descripción de la oferta de prueba");
        jobOfferDto.setSalary(20000.00);
        jobOfferDto.setRequirements("Requisitos de la oferta de prueba");
        jobOfferDto.setLocation("Málaga");
        jobOfferDto.setCreatedAt(LocalDate.now());



        //Llamamos al método a testear
        assertThrows(RuntimeException.class, () -> jobOfferService.createJobOffer(jobOfferDto, 1L));

        verify(companyRepository).findById(1L);
    }

    @Test
    void getJobOffers_Success(){
        //get all job offers
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(1L);
        jobOffer.setTitle("Oferta de prueba");
        jobOffer.setDescription("Descripción de la oferta de prueba");
        jobOffer.setSalary(20000.00);
        jobOffer.setRequirements("Requisitos de la oferta de prueba");
        jobOffer.setLocation("Málaga");
        jobOffer.setCreatedAt(LocalDate.now());

        when(jobOfferRepository.findAll()).thenReturn(List.of(jobOffer));
        when(jobApplicationRepository.countApplicationsByJobOfferId(1L)).thenReturn(5);

        List<JobOffer> jobOffers = jobOfferService.getJobOffers();

        assertNotNull(jobOffers);
        assertEquals(1, jobOffers.size());
        assertEquals(5, jobOffers.get(0).getNumberOfApplications());

        verify(jobOfferRepository).findAll();
        verify(jobApplicationRepository).countApplicationsByJobOfferId(1L);
    }

    @Test
    void getJobOffers_Empty(){
        when(jobOfferRepository.findAll()).thenReturn(List.of());

        List<JobOffer> jobOffers = jobOfferService.getJobOffers();

        assertNotNull(jobOffers);
        assertTrue(jobOffers.isEmpty());

        verify(jobOfferRepository).findAll();
    }

    @Test
    void updateJobOffers_Success(){
        JobOffer existingJobOffer = new JobOffer();
        existingJobOffer.setId(1L);
        existingJobOffer.setTitle("Oferta de antigua");

        JobOfferDto jobOfferDto = new JobOfferDto();
        jobOfferDto.setTitle("Oferta de actualizada");
        jobOfferDto.setDescription("Descripción de la oferta de prueba actualizada");
        jobOfferDto.setSalary(25000.00);
        jobOfferDto.setRequirements("Requisitos de la oferta de prueba actualizada");
        jobOfferDto.setLocation("Zaragoza");

        when(jobOfferRepository.findById(1L)).thenReturn(Optional.of(existingJobOffer));
        when(jobOfferRepository.save(any(JobOffer.class))).thenReturn(existingJobOffer);

        JobOffer updatedJobOffer = jobOfferService.updateJobOffer(1L, jobOfferDto);

        assertNotNull(updatedJobOffer);
        assertEquals(jobOfferDto.getTitle(), updatedJobOffer.getTitle());
        assertEquals(jobOfferDto.getDescription(), updatedJobOffer.getDescription());
        assertEquals(jobOfferDto.getSalary(), updatedJobOffer.getSalary());
        assertEquals(jobOfferDto.getRequirements(), updatedJobOffer.getRequirements());
        assertEquals(jobOfferDto.getLocation(), updatedJobOffer.getLocation());

        verify(jobOfferRepository).findById(1L);
        verify(jobOfferRepository).save(any(JobOffer.class));

    }

    @Test
    void updateJobOffer_NotFound(){
        when(jobOfferRepository.findById(1L)).thenReturn(Optional.empty());

        JobOfferDto jobOfferDto = new JobOfferDto();
        jobOfferDto.setTitle("Oferta de actualizada");
        jobOfferDto.setDescription("Descripción de la oferta de prueba actualizada");
        jobOfferDto.setSalary(25000.00);
        jobOfferDto.setRequirements("Requisitos de la oferta de prueba actualizada");
        jobOfferDto.setLocation("Zaragoza");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            jobOfferService.updateJobOffer(1L, jobOfferDto);
        });

        assertEquals("No se ha encontrado la oferta de trabajo", exception.getMessage());
        verify(jobOfferRepository).findById(1L);

    }

    @Test
    void deleteJobOffer_Success(){
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(1L);
        jobOffer.setTitle("Oferta de prueba");

        when(jobOfferRepository.findById(1L)).thenReturn(Optional.of(jobOffer));

        JobOffer deletedJobOffer = jobOfferService.deleteJobOffer(1L);

        assertNotNull(deletedJobOffer);
        assertEquals(jobOffer, deletedJobOffer);

        verify(jobOfferRepository).findById(1L);
        verify(jobOfferRepository).delete(jobOffer);
    }

    @Test
    void deleteJobOffer_NotFound(){
        when(jobOfferRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            jobOfferService.deleteJobOffer(1L);
        });

        assertEquals("No se ha encontrado la oferta de trabajo", exception.getMessage());
        verify(jobOfferRepository).findById(1L);
    }


    @Test
    void getJobOfferById_Success(){
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(1L);
        jobOffer.setTitle("Oferta de prueba");

        when(jobOfferRepository.findById(1L)).thenReturn(Optional.of(jobOffer));

        JobOffer foundJobOffer = jobOfferService.getJobOfferById(1L);

        assertNotNull(foundJobOffer);
        assertEquals(jobOffer, foundJobOffer);

        verify(jobOfferRepository).findById(1L);
    }

    @Test
    void getJobOfferById_NotFound() {
        when(jobOfferRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            jobOfferService.getJobOfferById(1L);
        });

        assertEquals("No se ha encontrado la oferta de trabajo con ID: 1", exception.getMessage());
        verify(jobOfferRepository).findById(1L);
    }

    @Test
    void getJobOffersByCompany_Success(){
        Company company = new Company();
        company.setId(1L);


        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(1L);
        jobOffer.setCompany(company);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(jobOfferRepository.findByCompany(company)).thenReturn(List.of(jobOffer));

        List<JobOffer> jobOffers = jobOfferService.getJobOffersByCompany(1L);

        assertNotNull(jobOffers);
        assertEquals(1, jobOffers.size());
        assertEquals(1L, jobOffers.get(0).getId());

        verify(companyRepository).findById(1L);
        verify(jobOfferRepository).findByCompany(company);
    }

    @Test
    void getJobOffersByCompany_CompanyNotFound(){
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            jobOfferService.getJobOffersByCompany(1L);
        });

        assertEquals("No se ha encontrado la empresa con ID: 1", exception.getMessage());
        verify(companyRepository).findById(1L);
    }
}