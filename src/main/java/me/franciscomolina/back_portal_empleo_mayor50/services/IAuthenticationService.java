package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;

public interface IAuthenticationService {
    //método para iniciar sesión y devolver JWT
    UserEntity signInAndReturnJWT(UserEntity signInRequest);
}
