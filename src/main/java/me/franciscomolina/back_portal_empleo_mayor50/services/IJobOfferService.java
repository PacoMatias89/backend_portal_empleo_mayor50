package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.JobOfferDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;

import java.util.List;

public interface IJobOfferService {



    JobOffer createJobOffer(JobOfferDto jobOfferDto, Long id);

    List<JobOffer> getJobOffers();

    JobOffer updateJobOffer(Long id, JobOfferDto jobOffer);

    JobOffer deleteJobOffer(Long id);

    JobOffer getJobOfferById(Long id);

    List<JobOffer> getJobOffersByCompany(Long id);

    List<JobOffer>getJobOfferByIdCompany(Long id);

    int getTotalViewsByCompany(Long companyId);

    void incrementViews(Long jobId);

    int countApplicationsByCompanyId(Long companyId);

    int countJobOffersByCompanyId(Long companyId);

    boolean existsById(Long jobId);
}
