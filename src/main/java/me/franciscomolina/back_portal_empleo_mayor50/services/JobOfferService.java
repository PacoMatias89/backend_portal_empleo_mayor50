package me.franciscomolina.back_portal_empleo_mayor50.services;


import me.franciscomolina.back_portal_empleo_mayor50.dto.JobOfferDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.CompanyRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobApplicationRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JobOfferService implements IJobOfferService {

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Override
    public JobOffer createJobOffer(JobOfferDto jobOfferDto, Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la empresa " ));

        JobOffer jobOffer = new JobOffer();
        jobOffer.setTitle(jobOfferDto.getTitle());
        jobOffer.setDescription(jobOfferDto.getDescription());
        jobOffer.setSalary(jobOfferDto.getSalary());
        jobOffer.setRequirements(jobOfferDto.getRequirements());
        jobOffer.setLocation(jobOfferDto.getLocation());
        jobOffer.setCreatedAt(LocalDate.now());
        jobOffer.setCompany(company); // Asigna la empresa a la oferta de trabajo

        return jobOfferRepository.save(jobOffer);
    }

    @Override
    public List<JobOffer> getJobOffers() {
        List<JobOffer> jobOffers = jobOfferRepository.findAll();

        // Contar las candidaturas para cada oferta
        jobOffers.forEach(jobOffer -> {
            int numberOfApplications = jobApplicationRepository.countApplicationsByJobOfferId(jobOffer.getId());
            jobOffer.setNumberOfApplications(numberOfApplications);
        });

        return jobOffers;
    }


    @Override
    public JobOffer updateJobOffer(Long id, JobOfferDto jobOffer) {
        Optional<JobOffer> jobOfferOptional = jobOfferRepository.findById(id);

        if (jobOfferOptional.isPresent()) {
            JobOffer jobOfferToUpdate = jobOfferOptional.get();
            jobOfferToUpdate.setTitle(jobOffer.getTitle());
            jobOfferToUpdate.setDescription(jobOffer.getDescription());
            jobOfferToUpdate.setSalary(jobOffer.getSalary());
            jobOfferToUpdate.setRequirements(jobOffer.getRequirements());
            jobOfferToUpdate.setLocation(jobOffer.getLocation());
            return jobOfferRepository.save(jobOfferToUpdate);
        }else {
            throw new RuntimeException("No se ha encontrado la oferta de trabajo");
        }

    }

    @Override
    public JobOffer deleteJobOffer(Long id) {
        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la oferta de trabajo"));

        jobOfferRepository.delete(jobOffer);
        return jobOffer;
    }

    @Override
    public JobOffer getJobOfferById(Long id) {

        return jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la oferta de trabajo con ID: " + id));
    }

    @Override
    public List<JobOffer> getJobOffersByCompany(Long id) {
        return jobOfferRepository.findByCompany(companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la empresa con ID: " + id)));
    }
}
