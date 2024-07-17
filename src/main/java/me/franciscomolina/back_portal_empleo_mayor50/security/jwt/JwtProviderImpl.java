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
    private String JWT_EXPIRATION_IN_MS;

    @Override
    public String generateToken(UserEntityPrincipal auth) {
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();
        Date expirationDate = new Date(now.toEpochMilli() + Long.parseLong(JWT_EXPIRATION_IN_MS));

        return Jwts.builder()
                .setSubject(auth.getUsername())
                .claim("roles", authorities)
                .claim("userId", auth.getId())
                .claim("type", "user") // Añadimos el tipo
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public String generateTokenClient(UserEntity client) {
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        if (JWT_EXPIRATION_IN_MS == null || JWT_EXPIRATION_IN_MS.isEmpty()) {
            return Jwts.builder()
                    .setSubject(client.getLastnames())
                    .claim("roles", client.getRole())
                    .claim("userId", client.getId())
                    .claim("type", "user") // Añadimos el tipo
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        } else {
            return Jwts.builder()
                    .setSubject(client.getLastnames())
                    .claim("roles", client.getRole())
                    .claim("userId", client.getId())
                    .claim("type", "user") // Añadimos el tipo
                    .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(JWT_EXPIRATION_IN_MS)))
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        }
    }

    @Override
    public String generateTokenToCompany(CompanyEntityPrincipal auth) {
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();
        Date expirationDate = new Date(now.toEpochMilli() + Long.parseLong(JWT_EXPIRATION_IN_MS));

        return Jwts.builder()
                .setSubject(auth.getUsername())
                .claim("roles", authorities)
                .claim("userId", auth.getId())
                .claim("type", "company") // Añadimos el tipo
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public String generateTokenCompany(Company client) {
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        if (JWT_EXPIRATION_IN_MS == null || JWT_EXPIRATION_IN_MS.isEmpty()) {
            return Jwts.builder()
                    .setSubject(client.getLastname())
                    .claim("roles", client.getRole())
                    .claim("userId", client.getId())
                    .claim("type", "company") // Añadimos el tipo
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        } else {
            return Jwts.builder()
                    .setSubject(client.getLastname())
                    .claim("roles", client.getRole())
                    .claim("userId", client.getId())
                    .claim("type", "company") // Añadimos el tipo
                    .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(JWT_EXPIRATION_IN_MS)))
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        }
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        Claims claims = extractClaims(request);
        if (claims == null) {
            return null;
        }

        String username = claims.getSubject();
        long userId = claims.get("userId", Long.class);
        String type = claims.get("type", String.class); // Obtenemos el tipo
        Set<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SecurityUtils::convertToAuthority)
                .collect(Collectors.toSet());

        UserDetails userDetails;
        if ("user".equals(type)) {
            userDetails = UserEntityPrincipal.builder()
                    .username(username)
                    .authorities(authorities)
                    .id(userId)
                    .build();
        } else if ("company".equals(type)) {
            userDetails = CompanyEntityPrincipal.builder()
                    .username(username)
                    .authorities(authorities)
                    .id(userId)
                    .build();
        } else {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    private Claims extractClaims(HttpServletRequest request) {
        String token = SecurityUtils.extractAuthTokenFromRequest(request);

        if (token == null) {
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

        return !claims.getExpiration().before(new Date());
    }
}
