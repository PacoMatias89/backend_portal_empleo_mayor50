package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.CompanyDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;

import java.util.Optional;

public interface ICompanyService {
    Company create(CompanyDto company);

    Optional<Company> findByUsername(String name);

    Optional<Company> findByEmail(String email);

    Company findByNameReturnToken(String name);

    Company editCompany(Long id, CompanyDto companyEntity);

    Company deleteCompany(Long id);


}
