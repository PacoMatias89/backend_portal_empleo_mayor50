package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationDTO;

import java.util.List;

public interface IJobApplicationService {


    JobApplicationDTO applyToJob(JobApplicationDTO jobApplicationDTO, Long idJobOffer, Long idUser);

    List<JobApplicationDTO> getJobApplication(Long id);
}
