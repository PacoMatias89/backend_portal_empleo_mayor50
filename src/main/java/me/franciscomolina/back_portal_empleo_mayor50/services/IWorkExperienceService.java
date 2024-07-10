package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.WorkExperienceDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.WorkExperience;

import java.util.List;
import java.util.Optional;

public interface IWorkExperienceService {

    List<WorkExperience> getWorkExperienceAll();
    List<WorkExperience> getuserWorkExperience(Long userId);
    Optional<WorkExperience> getWorkExperienceById(Long id);
    WorkExperience saveWorkExperience(WorkExperienceDto workExperienceDto);
    WorkExperience updateWorkExperience(Long id, WorkExperienceDto workExperience);
    WorkExperience deleteWorkExperience(Long id);

    //Método para calcular toda la experiencia del usuario

    String calculateTotalExperience(List<WorkExperience> workExperiences);
}
