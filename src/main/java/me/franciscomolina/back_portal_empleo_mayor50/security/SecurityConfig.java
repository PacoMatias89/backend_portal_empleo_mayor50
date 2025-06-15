package me.franciscomolina.back_portal_empleo_mayor50.security;

import me.franciscomolina.back_portal_empleo_mayor50.security.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomCompanyDetailsService customCompanyDetailsService;

    @Autowired
    private CustomAdminDetailService customAdminDetailsService;  // Servicio para Admin

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        auth.userDetailsService(customCompanyDetailsService).passwordEncoder(passwordEncoder);
        auth.userDetailsService(customAdminDetailsService).passwordEncoder(passwordEncoder);

        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/api/authentication/sign-up").permitAll()
                        .requestMatchers("/api/authentication/sign-in").permitAll()
                        .requestMatchers("/api/authentication-company/sign-up").permitAll()
                        .requestMatchers("/api/authentication-company/sign-in").permitAll()
                        .requestMatchers("/api/company/job-offers/getAllJobOffer").permitAll()

                        // Endpoints protegidos para subir y descargar archivos
                        .requestMatchers("/api/files/upload").hasAnyRole("USER", "COMPANY", "ADMIN")
                        .requestMatchers("/api/files/download/**").hasAnyRole("USER", "COMPANY", "ADMIN")
                        .requestMatchers("/api/files/exists/**").hasAnyRole("COMPANY", "ADMIN")

                        .requestMatchers("/api/user/update/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/user/delete").hasRole("ADMIN")
                        .requestMatchers("/api/company/update/**").hasAnyRole("COMPANY", "ADMIN")
                        .requestMatchers("/api/company/delete").hasRole("ADMIN")
                        .requestMatchers("/api/user/job-applications").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/company/job-application/user/**").hasRole("ADMIN")
                        .requestMatchers("/api/company/job-offers").hasAnyRole("COMPANY", "ADMIN")
                        .requestMatchers("/api/company/job-application/updateJobApplicationStatus/**").hasAnyRole("COMPANY", "ADMIN")
                        .requestMatchers("/api/work-experience/get_work-experiences").hasAnyRole("USER", "COMPANY", "ADMIN")
                        .requestMatchers("/api/company/job-offers/getJobOfferById/**").hasAnyRole("COMPANY", "ADMIN")
                        .requestMatchers("/api/company/job-application/getJobApplicationsCompany").hasAnyRole("COMPANY", "ADMIN")
                        .requestMatchers("/api/company/job-application/job-offer/{jobOfferId}/applications").hasAnyRole("COMPANY", "ADMIN")
                        .requestMatchers("/api/company/job-application/job-offers-with-applicants").hasAnyRole("COMPANY", "ADMIN")
                        .requestMatchers("/api/company/job-offer/createJobOffer").hasAnyRole("COMPANY", "ADMIN")
                        .requestMatchers("/api/company/jobOffers").hasAnyRole("COMPANY", "ADMIN")
                        .requestMatchers("/api/company/jobOffersCount").hasAnyRole("COMPANY", "ADMIN")
                        .requestMatchers("/api/company/totalViews").hasAnyRole("COMPANY", "ADMIN")
                        .requestMatchers("/api/company/applications").hasAnyRole("COMPANY", "ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Configuración de los UserDetailsService y passwordEncoder para todos los tipos
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        auth.userDetailsService(customCompanyDetailsService).passwordEncoder(passwordEncoder);
        auth.userDetailsService(customAdminDetailsService).passwordEncoder(passwordEncoder);
    }

    // Configuración CORS para el frontend
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("https://portal-empleo.netlify.app")
                        //.allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}
