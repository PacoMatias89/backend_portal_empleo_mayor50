package me.franciscomolina.back_portal_empleo_mayor50.security;

import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.services.IUserService;
import me.franciscomolina.back_portal_empleo_mayor50.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity client = userService.findByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException("El usuario no fue encontrado:"+username));

        Set<GrantedAuthority> authorities = Set.of(SecurityUtils.convertToAuthority(client.getRole().name()));
        return UserEntityPrincipal.builder()
                .user(client)
                .id(client.getId())
                .username(username)
                .password(client.getPassword())
                .authorities(authorities)
                .build();
    }
}
