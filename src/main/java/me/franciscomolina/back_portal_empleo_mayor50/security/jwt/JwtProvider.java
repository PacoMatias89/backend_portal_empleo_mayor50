package me.franciscomolina.back_portal_empleo_mayor50.security.jwt;


import jakarta.servlet.http.HttpServletRequest;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import org.springframework.security.core.Authentication;



public interface JwtProvider {

    /*User*/
    String generateToken(UserEntityPrincipal auth);
    String generateTokenClient(UserEntity client);



    Authentication getAuthentication(HttpServletRequest request);
    boolean isTokenValid(HttpServletRequest request);

}
