package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationDTO;
import me.franciscomolina.back_portal_empleo_mayor50.dto.JobOfferDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobApplicationRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobOfferRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService implements IJobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public JobApplicationDTO applyToJob(JobApplicationDTO jobApplicationDTO, Long idJobOffer, Long idUser) {
        JobApplication existingJobApplication = jobApplicationRepository.findByJobOfferIdAndUserId(idJobOffer, idUser);
        if (existingJobApplication != null) {
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

        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        JobOfferDto jobOfferDto = new JobOfferDto(
                jobOffer.getId(),
                jobOffer.getTitle(),
                jobOffer.getDescription(),
                jobOffer.getSalary(),
                jobOffer.getRequirements(),
                jobOffer.getLocation(),
                jobOffer.getCreatedAt(),
                jobOffer.getCompany() != null ? jobOffer.getCompany().getId() : null
        );

        JobApplicationDTO responseDTO = new JobApplicationDTO(
                savedJobApplication.getId(),
                savedJobApplication.getUser().getId(),
                jobOfferDto
        );

        return responseDTO;
    }

    @Override
    public List<JobApplicationDTO> getJobApplication(Long userId) {
        List<JobApplication> jobApplications = jobApplicationRepository.findByUserId(userId);

        return jobApplications.stream().map(application -> {
            JobOffer jobOffer = application.getJobOffer();
            JobOfferDto jobOfferDto = new JobOfferDto(
                    jobOffer.getId(),
                    jobOffer.getTitle(),
                    jobOffer.getDescription(),
                    jobOffer.getSalary(),
                    jobOffer.getRequirements(),
                    jobOffer.getLocation(),
                    jobOffer.getCreatedAt(),
                    jobOffer.getCompany() != null ? jobOffer.getCompany().getId() : null
            );
            return new JobApplicationDTO(
                    application.getId(),
                    application.getUser().getId(),
                    jobOfferDto
            );
        }).collect(Collectors.toList());
    }
}
