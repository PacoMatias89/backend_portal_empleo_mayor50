package me.franciscomolina.back_portal_empleo_mayor50.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.security.AdminEntityPrincipal;
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

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private Date getExpirationDate() {
        long expiration = Long.parseLong(JWT_EXPIRATION_IN_MS);
        return new Date(System.currentTimeMillis() + expiration);
    }

    // TOKEN USER
    @Override
    public String generateToken(UserEntityPrincipal auth) {
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(auth.getUsername())
                .claim("roles", authorities)
                .claim("userId", auth.getId())
                .claim("type", "user")
                .setExpiration(getExpirationDate())
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public String generateTokenClient(UserEntity client) {
        return Jwts.builder()
                .setSubject(client.getLastnames())
                .claim("roles", client.getRole())
                .claim("userId", client.getId())
                .claim("type", "user")
                .setExpiration(getExpirationDate())
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // TOKEN COMPANY
    @Override
    public String generateTokenToCompany(CompanyEntityPrincipal auth) {
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(auth.getUsername())
                .claim("roles", authorities)
                .claim("userId", auth.getId())
                .claim("type", "company")
                .setExpiration(getExpirationDate())
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public String generateTokenCompany(Company company) {
        return Jwts.builder()
                .setSubject(company.getLastname())
                .claim("roles", company.getRole())
                .claim("userId", company.getId())
                .claim("type", "company")
                .setExpiration(getExpirationDate())
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // TOKEN ADMIN
    public String generateTokenAdmin(AdminEntityPrincipal auth) {
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(auth.getUsername())
                .claim("roles", authorities)
                .claim("userId", auth.getId())
                .claim("type", "admin")
                .setExpiration(getExpirationDate())
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateTokenAdminEntity(UserEntity admin) {
        return Jwts.builder()
                .setSubject(admin.getName())  // Cambia a tu campo correcto si es distinto
                .claim("roles", admin.getRole())
                .claim("userId", admin.getId())
                .claim("type", "admin")
                .setExpiration(getExpirationDate())
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        Claims claims = extractClaims(token);
        return claims != null && claims.getExpiration().after(new Date());
    }

    @Override
    public Authentication getAuthentication(String token) {
        Claims claims = extractClaims(token);
        if (claims == null) return null;

        String username = claims.getSubject();
        Long userId = claims.get("userId", Long.class);
        String type = claims.get("type", String.class);

        Set<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SecurityUtils::convertToAuthority)
                .collect(Collectors.toSet());

        UserDetails userDetails;

        if ("user".equalsIgnoreCase(type)) {
            userDetails = UserEntityPrincipal.builder()
                    .username(username)
                    .authorities(authorities)
                    .id(userId)
                    .build();
        } else if ("company".equalsIgnoreCase(type)) {
            userDetails = CompanyEntityPrincipal.builder()
                    .username(username)
                    .authorities(authorities)
                    .id(userId)
                    .build();
        } else if ("admin".equalsIgnoreCase(type)) {
            userDetails = AdminEntityPrincipal.builder()
                    .username(username)
                    .authorities(authorities)
                    .id(userId)
                    .build();
        } else {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    private Claims extractClaims(String token) {
        if (token == null || !token.contains(".")) {
            System.out.println("Token inválido o malformado: " + token);
            return null;
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Error parseando token: " + token);
            e.printStackTrace();
            return null;
        }
    }
}
