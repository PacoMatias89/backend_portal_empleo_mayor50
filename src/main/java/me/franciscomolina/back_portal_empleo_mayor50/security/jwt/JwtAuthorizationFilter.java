package me.franciscomolina.back_portal_empleo_mayor50.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("Token recibido en filtro: " + token);

            try {
                if (jwtProvider.isTokenValid(token)) {
                    Authentication auth = jwtProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    System.out.println("Token inválido o expirado: " + token);
                }
            } catch (Exception e) {
                System.out.println("JWT inválido o malformado: " + token);
                e.printStackTrace();
            }
        } else {
            System.out.println("No se recibió token en header Authorization");
        }

        filterChain.doFilter(request, response);
    }


}
