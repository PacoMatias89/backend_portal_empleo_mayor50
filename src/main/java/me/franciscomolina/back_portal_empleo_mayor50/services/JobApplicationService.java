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
        // Verifica si el usuario ya ha aplicado a la oferta
        if (jobApplicationRepository.findByJobOfferIdAndUserId(idJobOffer, idUser).isPresent()) {
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

        JobApplicationStatus status = jobApplicationCreateDto.getStatus() != null
                ? JobApplicationStatus.valueOf(jobApplicationCreateDto.getStatus().toUpperCase())
                : JobApplicationStatus.INSCRITO;
        jobApplication.setStatus(status);

        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        return new JobApplicationCreateDto(
                savedJobApplication.getJobOffer().getId(),
                savedJobApplication.getUser().getId(),
                savedJobApplication.getStatus().name()
        );
    }

    @Override
    public List<JobApplication> getJobApplication(Long userId) {
        return jobApplicationRepository.findByUserId(userId);
    }

    @Override
    public List<JobApplication> getJobApplicationsByCompany(Long companyId) {
        return jobApplicationRepository.findByCompanyId(companyId);
    }

    @Override
    public JobApplicationUpdateStatusDto updateJobApplicationStatus(JobApplicationUpdateStatusDto statusDto, Long idJobOffer, Long idUser) {
        JobApplication jobApplication = jobApplicationRepository.findByJobOfferIdAndUserId(idJobOffer, idUser)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la solicitud de empleo"));

        try {
            JobApplicationStatus jobApplicationStatus = JobApplicationStatus.valueOf(statusDto.getStatus().toUpperCase());
            jobApplication.setStatus(jobApplicationStatus);
            jobApplicationRepository.save(jobApplication);
            return new JobApplicationUpdateStatusDto(jobApplication.getStatus().name());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado no válido: " + statusDto.getStatus());
        }
    }
}
