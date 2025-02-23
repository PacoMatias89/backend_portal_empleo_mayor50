package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationCreateDto;
import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationUpdateStatusDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.model.JobApplicationStatus;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobApplicationRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobOfferRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private JobOfferRepository jobOfferRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JobApplicationService jobApplicationService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void applyToJob_Success() {
        Long idJobOffer = 1L;
        Long idUser = 1L;

        // Configuración del DTO con el estado INSCRITO
        JobApplicationCreateDto dto = new JobApplicationCreateDto();
        dto.setStatus(JobApplicationStatus.INSCRITO.name());

        // Configuración de los objetos necesarios
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(idJobOffer); // Agregar el ID de la oferta
        UserEntity user = new UserEntity();
        user.setId(idUser);

        JobApplication jobApplication = new JobApplication();
        jobApplication.setUser(user);
        jobApplication.setJobOffer(jobOffer);
        jobApplication.setCreatedAt(LocalDate.now());
        jobApplication.setStatus(JobApplicationStatus.INSCRITO);

        // Configuración de los mocks
        when(jobApplicationRepository.findByJobOfferIdAndUserId(idJobOffer, idUser)).thenReturn(Optional.empty());
        when(jobOfferRepository.findById(idJobOffer)).thenReturn(Optional.of(jobOffer));
        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(jobApplication);

        // Llamada al método del servicio
        JobApplicationCreateDto response = jobApplicationService.applyToJob(dto, idJobOffer, idUser);

        // Validación del resultado
        assertEquals(idUser, response.getUserId());
        assertEquals(JobApplicationStatus.INSCRITO.name(), response.getStatus());

        // Verifica las interacciones con los mocks
        verify(jobApplicationRepository).findByJobOfferIdAndUserId(idJobOffer, idUser);
        verify(jobOfferRepository).findById(idJobOffer);
        verify(userRepository).findById(idUser);
        verify(jobApplicationRepository).save(any(JobApplication.class));
    }


    @Test
    void applyJob_DefaultStatus(){
        Long idJobOffer = 1L;
        Long idUser = 1L;

        // Configuración del DTO sin estado
        JobApplicationCreateDto dto = new JobApplicationCreateDto();
        dto.setStatus(null);

        // Configuración de los objetos necesarios
        JobOffer jobOffer = new JobOffer();
        UserEntity user = new UserEntity();
        user.setId(idUser);

        JobApplication jobApplication = new JobApplication();
        jobApplication.setUser(user);
        jobApplication.setJobOffer(jobOffer);
        jobApplication.setCreatedAt(LocalDate.now());
        jobApplication.setStatus(JobApplicationStatus.INSCRITO);

        // Configuración de los mocks
        when(jobApplicationRepository.findByJobOfferIdAndUserId(idJobOffer, idUser)).thenReturn(Optional.empty());
        when(jobOfferRepository.findById(idJobOffer)).thenReturn(Optional.of(jobOffer));
        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(jobApplication);

        // Llamada al método del servicio
        JobApplicationCreateDto response = jobApplicationService.applyToJob(dto, idJobOffer, idUser);

        // Validación del resultado
        assertEquals(idUser, response.getUserId());
        assertEquals(JobApplicationStatus.INSCRITO.name(), response.getStatus());

        // Verifica las interacciones con los mocks
        verify(jobApplicationRepository).findByJobOfferIdAndUserId(idJobOffer, idUser);
        verify(jobOfferRepository).findById(idJobOffer);
        verify(userRepository).findById(idUser);
        verify(jobApplicationRepository).save(any(JobApplication.class));
    }


    @Test
    void applyToJob_AlreadyApplied() {
        Long idJobOffer = 1L;
        Long idUser = 1L;

        // Configuración del DTO con el estado INSCRITO
        JobApplicationCreateDto dto = new JobApplicationCreateDto();
        dto.setStatus(JobApplicationStatus.INSCRITO.name());

        // Configuración de los mocks
        when(jobApplicationRepository.findByJobOfferIdAndUserId(idJobOffer, idUser)).thenReturn(Optional.of(new JobApplication()));

        // Llamada al método del servicio

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            jobApplicationService.applyToJob(dto, idJobOffer, idUser);
        });

        assertEquals("Ya has aplicado a esta oferta de trabajo", exception.getMessage());

        verify(jobApplicationRepository).findByJobOfferIdAndUserId(idJobOffer, idUser);
        verify(jobOfferRepository, never()).findById(anyLong());
        verify(userRepository, never()).findById(anyLong());
        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    void applyToJob_JobOfferNotFound() {
        Long idJobOffer = 1L;
        Long idUser = 1L;

        JobApplicationCreateDto dto = new JobApplicationCreateDto();
        dto.setStatus(JobApplicationStatus.INSCRITO.name());

        // Configuración de los mocks
        when(jobApplicationRepository.findByJobOfferIdAndUserId(idJobOffer, idUser)).thenReturn(Optional.empty());
        when(jobOfferRepository.findById(idJobOffer)).thenReturn(Optional.empty()); // Asegurarse de que devuelve Optional.empty()

        // Llamada al método del servicio
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            jobApplicationService.applyToJob(dto, idJobOffer, idUser);
        });

        assertEquals("No se ha encontrado la oferta de trabajo", exception.getMessage());

        verify(jobApplicationRepository).findByJobOfferIdAndUserId(idJobOffer, idUser);
        verify(jobOfferRepository).findById(idJobOffer);
        verify(userRepository, never()).findById(anyLong());
        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }


    @Test
    void applyToJob_UserNotFound() {
        Long idJobOffer = 1L;
        Long idUser = 1L;

        // Configuración del DTO con el estado INSCRITO
        JobApplicationCreateDto dto = new JobApplicationCreateDto();
        dto.setStatus(JobApplicationStatus.INSCRITO.name());

        // Configuración de los mocks
        when(jobApplicationRepository.findByJobOfferIdAndUserId(idJobOffer, idUser)).thenReturn(Optional.empty());
        when(jobOfferRepository.findById(idJobOffer)).thenReturn(Optional.of(new JobOffer()));
        when(userRepository.findById(idUser)).thenReturn(Optional.empty());

        // Llamada al método del servicio

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            jobApplicationService.applyToJob(dto, idJobOffer, idUser);
        });

        assertEquals("No se ha encontrado el usuario", exception.getMessage());

        verify(jobApplicationRepository).findByJobOfferIdAndUserId(idJobOffer, idUser);
        verify(jobOfferRepository).findById(idJobOffer);
        verify(userRepository).findById(idUser);
        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }


    @Test
    void updateJobApplicationStatus_Success(){
        Long idOffer = 1L;
        Long idUser = 1L;
        String status = JobApplicationStatus.CV_LEIDO.name();

        JobApplicationUpdateStatusDto dto = new JobApplicationUpdateStatusDto();
        dto.setStatus(status);

        JobApplication jobApplication = new JobApplication();
        jobApplication.setStatus(JobApplicationStatus.INSCRITO);

        when(jobApplicationRepository.findByJobOfferIdAndUserId(idOffer, idUser)).thenReturn(Optional.of(jobApplication));


        JobApplicationUpdateStatusDto response = jobApplicationService.updateJobApplicationStatus(dto, idOffer, idUser);

        assertEquals(status, response.getStatus());
        verify(jobApplicationRepository).findByJobOfferIdAndUserId(idOffer, idUser);
        verify(jobApplicationRepository).save(jobApplication);

    }


    @Test
    void updateJoApplicationStatus_JobapplicationsNotFound(){
        Long idOffer = 1L;
        Long idUser = 1L;
        String status = JobApplicationStatus.CV_LEIDO.name();

        JobApplicationUpdateStatusDto dto = new JobApplicationUpdateStatusDto();
        dto.setStatus(status);

        when(jobApplicationRepository.findByJobOfferIdAndUserId(idOffer, idUser)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            jobApplicationService.updateJobApplicationStatus(dto, idOffer, idUser);
        });

        assertEquals("No se ha encontrado la solicitud de empleo", exception.getMessage());
        verify(jobApplicationRepository).findByJobOfferIdAndUserId(idOffer, idUser);
        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

}