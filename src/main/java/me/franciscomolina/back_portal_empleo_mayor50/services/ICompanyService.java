package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.CompanyDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ICompanyService {


    Company deleteCompanyById(Long id);

    Company create(CompanyDto company);

    Optional<Company> findByUsername(String name);

    Optional<Company> findByEmail(String email);

    Company findByNameReturnToken(String name);

   // void editCompany(Long id, CompanyDto companyEntity);


    void updateCompanyPartial(Long id, Map<String, Object> updates);

    Company deleteCompany(Long id);

    List<Company> getAllCompanies();

    List<JobOffer> getJobOffers(Long id);
}
