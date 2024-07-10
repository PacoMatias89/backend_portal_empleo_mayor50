package me.franciscomolina.back_portal_empleo_mayor50.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.security.CompanyEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.util.SecurityUtils;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtProviderImpl implements JwtProvider {

    @Value("${app.jwt.secret}")
    private String JWT_SECRET;

    @Value("${app.jwt.expiration-in-ms}")
    private String JWT_EXPERATION_IN_MS;


    @Override
    public String generateToken(UserEntityPrincipal auth) {
        //Obtenemos las authorities del usuario, la convertimos en una cadena de texto separada por una coma.
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //Definimos la clave secreta para hacer la firma del token
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        // Obtener la fecha actual en milisegundos desde la época
        Instant now = Instant.now();
        Date expirationDate = new Date(now.toEpochMilli() + Long.parseLong(JWT_EXPERATION_IN_MS));

        return Jwts.builder()
                .setSubject(auth.getUsername())
                .claim("roles", authorities)
                .claim("userId", auth.getId())
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    @Override
    public String generateTokenClient(UserEntity client) {
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        if (JWT_EXPERATION_IN_MS == null || JWT_EXPERATION_IN_MS.isEmpty()) {
            // Si JWT_EXPERATION_IN_MS es null o está vacío, generar un token sin fecha de expiración
            return Jwts.builder()
                    .setSubject(client.getLastnames())
                    .claim("roles", client.getRole())
                    .claim("userId", client.getId())
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        } else {
            // Si JWT_EXPERATION_IN_MS tiene un valor, usarlo como fecha de expiración
            return Jwts.builder()
                    .setSubject(client.getLastnames())
                    .claim("roles", client.getRole())
                    .claim("userId", client.getId())
                    .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(JWT_EXPERATION_IN_MS)))
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        }
    }

    /*========================================================*/
    /*================= METHOD COMPANY ===============*/

    @Override
    public String generateTokenToCompany(CompanyEntityPrincipal auth) {
        //Obtenemos las authorities del usuario, la convertimos en una cadena de texto separada por una coma.
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //Definimos la clave secreta para hacer la firma del token
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        // Obtener la fecha actual en milisegundos desde la época
        Instant now = Instant.now();
        Date expirationDate = new Date(now.toEpochMilli() + Long.parseLong(JWT_EXPERATION_IN_MS));

        return Jwts.builder()
                .setSubject(auth.getUsername())
                .claim("roles", authorities)
                .claim("userId", auth.getId())
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public String generateTokenCompany(Company client) {
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        if (JWT_EXPERATION_IN_MS == null || JWT_EXPERATION_IN_MS.isEmpty()) {
            // Si JWT_EXPERATION_IN_MS es null o está vacío, generar un token sin fecha de expiración
            return Jwts.builder()
                    .setSubject(client.getLastname())
                    .claim("roles", client.getRole())
                    .claim("userId", client.getId())
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        } else {
            // Si JWT_EXPERATION_IN_MS tiene un valor, usarlo como fecha de expiración
            return Jwts.builder()
                    .setSubject(client.getLastname())
                    .claim("roles", client.getRole())
                    .claim("userId", client.getId())
                    .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(JWT_EXPERATION_IN_MS)))
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        }
    }


    @Override
    public Authentication getAuthentication(HttpServletRequest request) {

        Claims claims = extractClaims(request);
        //Para evitar que el get del claims pueda ser null
        if (claims == null){
            return null;
        }

        String username = claims.getSubject();
        long userId = claims.get("userId", Long.class);
        Set<GrantedAuthority> authorities= Arrays.stream(claims.get("roles").toString().split(","))
                .map(SecurityUtils::convertToAuthority)
                .collect(Collectors.toSet());

        UserDetails userDetails = UserEntityPrincipal.builder()
                .username(username)
                .authorities(authorities)
                .id(userId)
                .build();

        if (userDetails == null) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    private Claims extractClaims(HttpServletRequest request)
    {
        String token = SecurityUtils.extractAuthTokenFromRequest(request);

        if(token==null)
        {
            return null;
        }

        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    @Override
    public boolean isTokenValid(HttpServletRequest request) {
        Claims claims = extractClaims(request);

        if (claims == null) {
            return false;
        }

        if (claims.getExpiration().before(new Date())){
            return false;
        }
        return true;
    }
}
