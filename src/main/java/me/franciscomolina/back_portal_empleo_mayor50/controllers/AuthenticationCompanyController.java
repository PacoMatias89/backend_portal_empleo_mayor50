package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import jakarta.validation.Valid;
import me.franciscomolina.back_portal_empleo_mayor50.dto.CompanyDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.services.IAuthenticationServiceCompany;
import me.franciscomolina.back_portal_empleo_mayor50.services.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication-company")
public class AuthenticationCompanyController {

    @Autowired
    private IAuthenticationServiceCompany serviceCompany;

    @Autowired
    private ICompanyService companyService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody CompanyDto companyDto) {
        try {
            Company company = companyService.create(companyDto);
            if (company == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(company, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody Company companyDto) {
        try {
            return new ResponseEntity<>(serviceCompany.signInAndReturnJWTCompany(companyDto), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


}
