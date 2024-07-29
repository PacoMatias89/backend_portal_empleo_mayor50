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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService implements IJobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public JobApplicationCreateDto applyToJob(JobApplicationCreateDto jobApplicationCreateDto, Long idJobOffer, Long idUser) {
        Optional<JobApplication> existingJobApplication = jobApplicationRepository.findByJobOfferIdAndUserId(idJobOffer, idUser);
        if (existingJobApplication.isPresent()) {
            throw new RuntimeException("Ya has aplicado a esta oferta de trabajo");
        }

        JobOffer jobOffer = jobOfferRepository.findById(idJobOffer)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la oferta de trabajo"));

        UserEntity user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el usuario"));

        JobApplication jobApplication = new JobApplication();
        jobApplication.setUser(user);
        jobApplication.setJobOffer(jobOffer);
        jobApplication.setCreatedAt(LocalDate.now());

        //Compruebo que el estado de la oferta de trabajo no sea null y si lo es, lo cambio a INSCRITO
        JobApplicationStatus status = jobApplicationCreateDto.getStatus() != null
                ? JobApplicationStatus.valueOf(jobApplicationCreateDto.getStatus().toUpperCase())
                : JobApplicationStatus.INSCRITO;
        jobApplication.setStatus(status);

        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        JobApplicationCreateDto responseDTO = new JobApplicationCreateDto(
                savedJobApplication.getJobOffer().getId(), // ID de la oferta de trabajo
                savedJobApplication.getUser().getId(), // ID del usuario
                savedJobApplication.getStatus().name()
        );

        return responseDTO;
    }

    @Override
    public List<JobApplication> getJobApplication(Long userId) {
        List<JobApplication> jobApplications = jobApplicationRepository.findByUserId(userId);

        return jobApplications;




    }

    @Override
    public JobApplicationUpdateStatusDto updateJobApplicationStatus(JobApplicationUpdateStatusDto status, Long idJobOffer, Long idUser) {
        JobApplication jobApplication = jobApplicationRepository.findByJobOfferIdAndUserId(idJobOffer, idUser)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la solicitud de empleo"));

        JobApplicationStatus jobApplicationStatus = JobApplicationStatus.valueOf(status.getStatus().toUpperCase());
        jobApplication.setStatus(jobApplicationStatus);
        jobApplicationRepository.save(jobApplication);

        // Devuelve el DTO actualizado
        return new JobApplicationUpdateStatusDto(jobApplication.getStatus().name());
    }

}
