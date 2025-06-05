package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.UserDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    UserEntity create(UserDto client);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    UserEntity findByUnernameReturnToken(String username);


    UserEntity editClient(Long id, UserDto clientEntity);

    UserEntity deleteClient(Long id);

    List<JobApplication> getJobApplications(Long id);

    List<UserEntity>getAllUsers();
}
