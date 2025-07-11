package me.franciscomolina.back_portal_empleo_mayor50.services;


import me.franciscomolina.back_portal_empleo_mayor50.dto.UserDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.model.Role;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.FavoriteJobsRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.JobApplicationRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.UserRepository;
import me.franciscomolina.back_portal_empleo_mayor50.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteJobsRepository favoriteJobsRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;


    @Override
    public UserEntity create(UserDto client) {

        if (!client.getPassword().equals(client.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }


        UserEntity user = new UserEntity();
        user.setName(client.getName());
        user.setLastnames(client.getLastnames());
        user.setEmail(client.getEmail());
        user.setBirthdate(client.getBirthdate());
        user.setPassword(passwordEncoder.encode(client.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(client.getConfirmPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDate.now());//fecha de creación del usuario


        UserEntity userCreated = userRepository.save(user);
        String jwtToken = jwtProvider.generateTokenClient(userCreated);
        userCreated.setToken(jwtToken);


        return userCreated;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByName(username);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        Optional<UserEntity> userEmail = userRepository.findByEmail(email);
        if (userEmail.isPresent()) {
            return userEmail;
        }else {
            throw new IllegalArgumentException("El email no existe");
        }

    }

    @Override
    public UserEntity findByUnernameReturnToken(String username) {
        UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado: " + username));
        String jwt = jwtProvider.generateTokenClient(user);
        user.setToken(jwt);
        return user;
    }


    @Override
    public UserEntity editClient(Long id, UserDto clientDto) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado: " + id));

        // Actualizar solo si no es null
        if (clientDto.getName() != null) {
            user.setName(clientDto.getName());
        }

        if (clientDto.getLastnames() != null) {
            user.setLastnames(clientDto.getLastnames());
        }

        if (clientDto.getEmail() != null) {
            user.setEmail(clientDto.getEmail());
        }

        if (clientDto.getBirthdate() != null) {
            user.setBirthdate(clientDto.getBirthdate());
        }

        // Para password solo actualizar si ambos vienen y coinciden
        String password = clientDto.getPassword();
        String confirmPassword = clientDto.getConfirmPassword();

        if (password != null || confirmPassword != null) {
            if (password == null || confirmPassword == null) {
                throw new IllegalArgumentException("Ambos campos de contraseña deben estar presentes para cambiar la contraseña");
            }
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }

            user.setPassword(passwordEncoder.encode(password));
            user.setConfirmPassword(passwordEncoder.encode(confirmPassword));
        }

        return userRepository.save(user);
    }


    @Transactional
    @Override
    public UserEntity deleteClient(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado: " + id));

        // ✅ Eliminar favoritos por ID
        favoriteJobsRepository.deleteAllByUserId(id);

        // Eliminar aplicaciones a empleos (si aplica)
        jobApplicationRepository.deleteByUser(user); // Si tienes esa relación

        // Ahora sí, eliminar usuario
        userRepository.delete(user);

        return user;
    }



    @Override
    public List<JobApplication> getJobApplications(Long id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado: " + id));

        return jobApplicationRepository.findByUser(user);



    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}
