package me.franciscomolina.back_portal_empleo_mayor50.services;


import me.franciscomolina.back_portal_empleo_mayor50.dto.UserDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.model.Role;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.UserRepository;
import me.franciscomolina.back_portal_empleo_mayor50.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;


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
    public UserEntity editClient(Long id, UserDto clientEntity) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado: " + id));

        user.setName(clientEntity.getName());
        user.setLastnames(clientEntity.getLastnames());
        user.setEmail(clientEntity.getEmail());
        user.setBirthdate(clientEntity.getBirthdate());

        //Si la contraseña se cambia se encripta

        user.setPassword(passwordEncoder.encode(clientEntity.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(clientEntity.getConfirmPassword()));


        return userRepository.save(user);
    }

    @Override
    public UserEntity deleteClient(Long id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no fue encontrado: " + id));

        userRepository.deleteById(id);

        return user;
    }
}
