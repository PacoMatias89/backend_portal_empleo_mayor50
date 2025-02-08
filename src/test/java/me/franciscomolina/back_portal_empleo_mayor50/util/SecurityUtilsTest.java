package me.franciscomolina.back_portal_empleo_mayor50.util;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityUtilsTest {

    @Test
    void convertToAuthority_WithRolePrefix(){
        String role = "ROLE_ADMIN";
        SimpleGrantedAuthority authority = SecurityUtils.convertToAuthority(role);

        assertNotNull(authority);
        assertEquals("ROLE_ADMIN", authority.getAuthority());
    }

    @Test
    void convertToAuthority_WithoutRolePrefix(){
        String role = "ADMIN";
        SimpleGrantedAuthority authority = SecurityUtils.convertToAuthority(role);

        assertNotNull(authority);
        assertEquals("ROLE_ADMIN", authority.getAuthority());
    }

    @Test
    void extractAuthTokenFromRequest_WithValidToken(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(SecurityUtils.AUTH_HEADER)).thenReturn("Bearer validToken123");

        String token = SecurityUtils.extractAuthTokenFromRequest(request);

        assertNotNull(token);
        assertEquals("validToken123", token);
    }

    @Test
    void extractAuthTokenFromRequest_WithInvalidToken(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(SecurityUtils.AUTH_HEADER)).thenReturn("invalidToken");

        String token = SecurityUtils.extractAuthTokenFromRequest(request);

        assertNull(token);
    }

    @Test
    void extractAuthTokenFromRequest_WithNoToken(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(SecurityUtils.AUTH_HEADER)).thenReturn(null);

        String token = SecurityUtils.extractAuthTokenFromRequest(request);

        assertNull(token);
    }

    @Test
    void extractAuthTokenFromRequest_WithEmptyToken(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(SecurityUtils.AUTH_HEADER)).thenReturn("");

        String token = SecurityUtils.extractAuthTokenFromRequest(request);

        assertNull(token);
    }

}