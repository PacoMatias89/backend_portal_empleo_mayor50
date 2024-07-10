package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;

public interface IAuthenticationServiceCompany {

    Company signInAndReturnJWTCompany(Company signInRequest);
}
