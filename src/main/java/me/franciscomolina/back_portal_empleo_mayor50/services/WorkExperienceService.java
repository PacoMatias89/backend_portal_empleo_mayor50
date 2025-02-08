package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.WorkExperienceDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.WorkExperience;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.UserRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.WorkExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class WorkExperienceService implements IWorkExperienceService {

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    @Autowired
    private IFileStoraService fileStorageService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<WorkExperience> getWorkExperienceAll() {
        return workExperienceRepository.findAll();
    }

    @Override
    public List<WorkExperience> getuserWorkExperience(Long userId) {
        return workExperienceRepository.findByUserId(userId);
    }

    @Override
    public Optional<WorkExperience> getWorkExperienceById(Long id) {
        return workExperienceRepository.findById(id);
    }
    @Override
    public WorkExperience saveWorkExperience(WorkExperienceDto workExperienceDto) {
        WorkExperience workExperience = new WorkExperience();
        workExperience.setCompanyName(workExperienceDto.getCompanyName());
        workExperience.setPosition(workExperienceDto.getPosition());

        // Validar que las fechas no sean nulas y no estén en el futuro
        LocalDate startDate = workExperienceDto.getStartDate();
        LocalDate endDate = workExperienceDto.getEndDate();

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }

        if (startDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio o fin no puede ser en el futuro");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser mayor a la fecha de fin");
        }

        // Asignar las fechas después de validar
        workExperience.setStartDate(startDate);
        workExperience.setEndDate(endDate);
        workExperience.setCreatedAt(LocalDate.now());

        workExperience.setDescription(workExperienceDto.getDescription());
        userRepository.findById(workExperienceDto.getUserId()).ifPresent(workExperience::setUser);

        // Si se ha subido un archivo, guardarlo en el sistema de archivos y almacenar la ruta
        if (workExperienceDto.getFile() != null && !workExperienceDto.getFile().isEmpty()) {
            try {
                String filePath = fileStorageService.saveFile(workExperienceDto.getFile(), workExperienceDto.getUserId());
                workExperience.setFilePath(filePath);  // Guardamos la ruta en la base de datos
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
            }
        }

        return workExperienceRepository.save(workExperience);
    }


    @Override
    public WorkExperience updateWorkExperience(Long id, WorkExperienceDto workExperienceDto) {
        Optional<WorkExperience> workExperienceOptional = workExperienceRepository.findById(id);

        if (workExperienceOptional.isPresent()) {
            WorkExperience workExperienceUpdate = workExperienceOptional.get();
            workExperienceUpdate.setCompanyName(workExperienceDto.getCompanyName());
            workExperienceUpdate.setPosition(workExperienceDto.getPosition());

            LocalDate startDate = workExperienceDto.getStartDate();
            LocalDate endDate = workExperienceDto.getEndDate();

            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
            }

            if (startDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha de inicio o fin no puede ser en el futuro");
            }

            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("La fecha de inicio no puede ser mayor a la fecha de fin");
            }

            workExperienceUpdate.setStartDate(startDate);
            workExperienceUpdate.setEndDate(endDate);

            workExperienceUpdate.setDescription(workExperienceDto.getDescription());
            userRepository.findById(workExperienceDto.getUserId()).ifPresent(workExperienceUpdate::setUser);

            return workExperienceRepository.save(workExperienceUpdate);
        } else {
            throw new RuntimeException("No se encontró la experiencia laboral con el id: " + id);
        }
    }

    @Override
    public WorkExperience deleteWorkExperience(Long id) {
        Optional<WorkExperience> workExperienceOptional = workExperienceRepository.findById(id);

        if (workExperienceOptional.isPresent()) {
            WorkExperience workExperience = workExperienceOptional.get();
            workExperienceRepository.delete(workExperience);
            return workExperience;
        } else {
            throw new RuntimeException("No se encontró la experiencia laboral con el id: " + id);
        }
    }

    @Override
    public String calculateTotalExperience(List<WorkExperience> workExperiences) {
        Period totalPeriod = Period.of(0, 0, 0);

        for (WorkExperience exp : workExperiences) {
            if (exp.getStartDate() != null && exp.getEndDate() != null) {
                Period period = Period.between(exp.getStartDate(), exp.getEndDate());
                totalPeriod = totalPeriod.plus(period);
            }
        }

        return totalPeriod.getYears() + " años " + totalPeriod.getMonths() + " meses " + totalPeriod.getDays() + " días";
    }
}
