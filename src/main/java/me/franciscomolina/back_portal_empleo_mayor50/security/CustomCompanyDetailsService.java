package me.franciscomolina.back_portal_empleo_mayor50.security;

import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.services.ICompanyService;
import me.franciscomolina.back_portal_empleo_mayor50.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomCompanyDetailsService implements UserDetailsService {

    @Autowired
    private ICompanyService companyService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Company company = companyService.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado:" + username));

        Set<GrantedAuthority> authorities = Set.of(SecurityUtils.convertToAuthority(company.getRole().name()));
        return CompanyEntityPrincipal.builder()
                .company(company)
                .id(company.getId())
                .username(username)
                .password(company.getPassword())
                .authorities(authorities)
                .build();
    }
}
