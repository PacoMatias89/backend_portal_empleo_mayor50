package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.CompanyDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.model.Role;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.CompanyRepository;
import me.franciscomolina.back_portal_empleo_mayor50.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class CompanyService implements ICompanyService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CompanyRepository companyRepository;


    @Override
    public Company create(CompanyDto company) {
        if (!company.getPassword().equals(company.getConfirmPasswordCompany())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        Company companyEntity = new Company();

        /*Datos para poder acceder al portal*/
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
        companyEntity.setRole(Role.USER);

        /*create date*/
        companyEntity.setCreatedAt(LocalDate.now());

        Company companyCreated = companyRepository.save(companyEntity);
        String jwtToken = jwtProvider.generateTokenCompany(companyCreated);
        companyCreated.setToken(jwtToken);

        return companyCreated;
    }

    @Override
    public Optional<Company> findByName(String name) {
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
    public Company editCompany(Long id, CompanyDto companyEntity) {
        return null;
    }

    @Override
    public Company deleteCompany(Long id) {
        return null;
    }
}
