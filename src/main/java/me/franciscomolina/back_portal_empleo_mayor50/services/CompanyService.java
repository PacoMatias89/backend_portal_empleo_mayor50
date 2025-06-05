package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.CompanyDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;


import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.model.Role;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.CompanyRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobApplicationRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobOfferRepository;
import me.franciscomolina.back_portal_empleo_mayor50.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService implements ICompanyService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private IJobOfferService jobOfferService;


    @Override
    public Company create(CompanyDto company) {
        if (!company.getPassword().equals(company.getConfirmPasswordCompany())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        Company companyEntity = new Company();

        /*Data to access the portal*/
        companyEntity.setName(company.getName());
        companyEntity.setLastname(company.getLastname());
        companyEntity.setEmail(company.getEmail());
        companyEntity.setPassword(passwordEncoder.encode(company.getPassword()));
        companyEntity.setConfirmPasswordCompany(passwordEncoder.encode(company.getConfirmPasswordCompany()));

        /*Data of the company*/
        companyEntity.setCompanyName(company.getCompanyName());
        companyEntity.setPhoneContact(company.getPhoneContact());
        companyEntity.setCifCompany(company.getCifCompany());
        companyEntity.setIsEtt(false);
        companyEntity.setDescription(company.getDescription());
        companyEntity.setRole(Role.COMPANY);

        /*create date*/
        companyEntity.setCreatedAt(LocalDate.now());

        Company companyCreated = companyRepository.save(companyEntity);
        String jwtToken = jwtProvider.generateTokenCompany(companyCreated);
        companyCreated.setToken(jwtToken);

        return companyCreated;
    }

    @Override
    public Optional<Company> findByUsername(String name) {
        return companyRepository.findByName(name);
    }

    @Override
    public Optional<Company> findByEmail(String email) {
        Optional<Company> userEmail = companyRepository.findByEmail(email);
        if (userEmail.isPresent()) {
            return userEmail;
        } else {
            throw new IllegalArgumentException("El email no existe");
        }

    }

    @Override
    public Company findByNameReturnToken(String name) {
        Company company = companyRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("La compañía no fue encontrado:" + name));
        String jwt = jwtProvider.generateTokenCompany(company);
        company.setToken(jwt);

        return company;
    }

    @Override
    public Company editCompany(Long id, CompanyDto companyDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La compañía no fue encontrada: " + id));

        // Actualización condicional solo si no es null ni vacío
        if (companyDto.getName() != null && !companyDto.getName().isBlank()) {
            company.setName(companyDto.getName());
        }

        if (companyDto.getLastname() != null && !companyDto.getLastname().isBlank()) {
            company.setLastname(companyDto.getLastname());
        }

        if (companyDto.getEmail() != null && !companyDto.getEmail().isBlank()) {
            company.setEmail(companyDto.getEmail());
        }

        // Solo actualizamos password si password y confirmPassword coinciden y no están vacíos
        if (companyDto.getPassword() != null && !companyDto.getPassword().isBlank()
                && companyDto.getConfirmPasswordCompany() != null && !companyDto.getConfirmPasswordCompany().isBlank()) {
            if (!companyDto.getPassword().equals(companyDto.getConfirmPasswordCompany())) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }
            company.setPassword(passwordEncoder.encode(companyDto.getPassword()));
            company.setConfirmPasswordCompany(passwordEncoder.encode(companyDto.getConfirmPasswordCompany()));
        }

        if (companyDto.getCompanyName() != null && !companyDto.getCompanyName().isBlank()) {
            company.setCompanyName(companyDto.getCompanyName());
        }

        if (companyDto.getPhoneContact() != null && !companyDto.getPhoneContact().isBlank()) {
            company.setPhoneContact(companyDto.getPhoneContact());
        }

        if (companyDto.getCifCompany() != null && !companyDto.getCifCompany().isBlank()) {
            company.setCifCompany(companyDto.getCifCompany());
        }

        // Para Boolean no tiene sentido chequear isBlank, solo null
        if (companyDto.getIsEtt() != null) {
            company.setIsEtt(companyDto.getIsEtt());
        }

        // Description puede ser null o vacía (no validada en tu DTO)
        if (companyDto.getDescription() != null) {
            company.setDescription(companyDto.getDescription());
        }

        return companyRepository.save(company);
    }


    @Override
    public Company deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La compañía no fue encontrado:" + id));

        companyRepository.delete(company);
        return company;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public List<JobOffer> getJobOffers(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            return jobOfferRepository.findByCompany(company.get());
        }else {
            throw new IllegalArgumentException("La compañía no fue encontrado:" + id);
        }


    }

    @Override
    public Company deleteCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La compañía no fue encontrado:" + id));

        // Eliminar ofertas de trabajo asociadas
        List<JobOffer> jobOffers = jobOfferRepository.findByCompany(company);
        for (JobOffer jobOffer : jobOffers) {
            jobOfferService.deleteJobOffer(jobOffer.getId());
        }

        // Eliminar la compañía
        companyRepository.delete(company);

        return company;
    }


}
