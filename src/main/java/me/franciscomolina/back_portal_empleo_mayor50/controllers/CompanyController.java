package me.franciscomolina.back_portal_empleo_mayor50.controllers;

import me.franciscomolina.back_portal_empleo_mayor50.dto.CompanyDto;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.services.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    @PutMapping("/update")
    public ResponseEntity<?> updateCompany(@AuthenticationPrincipal CompanyEntityPrincipal principal, @RequestBody CompanyDto companyDto) {
        if (principal == null) {
            return new ResponseEntity<>("Principal is null", HttpStatus.UNAUTHORIZED);
        }

        try {
            Long id = principal.getId();
            companyService.editCompany(id, companyDto);
            return new ResponseEntity<>("La empresa ha sido actualizada correctamente", HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
