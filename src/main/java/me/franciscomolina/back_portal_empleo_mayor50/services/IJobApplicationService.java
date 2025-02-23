package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationCreateDto;
import me.franciscomolina.back_portal_empleo_mayor50.dto.JobApplicationUpdateStatusDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;

import java.util.List;

public interface IJobApplicationService {


    JobApplicationCreateDto applyToJob(JobApplicationCreateDto jobApplicationCreateDto, Long idJobOffer, Long idUser);

    List<JobApplication> getJobApplication(Long id);

    List<JobApplication> getJobApplicationsByCompany(Long companyId);

    JobApplicationUpdateStatusDto updateJobApplicationStatus(JobApplicationUpdateStatusDto status, Long idJobOffer, Long idUser);
}
