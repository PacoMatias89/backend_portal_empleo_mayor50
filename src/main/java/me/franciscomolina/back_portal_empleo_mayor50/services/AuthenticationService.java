package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.entities.WorkExperience;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobApplicationRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.UserRepository;
import me.franciscomolina.back_portal_empleo_mayor50.security.UserEntityPrincipal;
import me.franciscomolina.back_portal_empleo_mayor50.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IWorkExperienceService workExperienceService;

    @Autowired
    private IJobApplicationService jobApplicationService;

    @Override
    public UserEntity signInAndReturnJWT(UserEntity signInRequest) {

        UserEntity client = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado:" + signInRequest.getEmail()));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(client.getName(), signInRequest.getPassword())
        );

        UserEntityPrincipal clientPrincipal = (UserEntityPrincipal) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(clientPrincipal);

        UserEntity sigInUser = clientPrincipal.getUser();
        sigInUser.setToken(jwt);

        //obtenemos la expriencia laboral
        // Obtener y calcular la experiencia laboral
       /* List<WorkExperience> workExperiences = workExperienceService.getuserWorkExperience(sigInUser.getId());
        if (workExperiences != null) {
            sigInUser.setWorkExperiences(workExperiences);
            String totalExperience = workExperienceService.calculateTotalExperience(workExperiences);
            sigInUser.setTotalExperience(totalExperience);
            System.out.println("Experiencia laboral recuperada: " + workExperiences);
        } else {
            System.out.println("No se encontró experiencia laboral para el usuario.");
        }*/



        //workExperienceService.calculateTotalExperience(workExperiences);
        return sigInUser;
    }
}