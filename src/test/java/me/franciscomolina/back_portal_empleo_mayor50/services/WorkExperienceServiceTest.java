package me.franciscomolina.back_portal_empleo_mayor50.services;

import me.franciscomolina.back_portal_empleo_mayor50.dto.WorkExperienceDto;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.entities.WorkExperience;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.UserRepository;
import me.franciscomolina.back_portal_empleo_mayor50.repositories.WorkExperienceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class WorkExperienceServiceTest {

    @Mock
    private WorkExperienceRepository workExperienceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WorkExperienceService workExperienceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWorkExperienceAll_Success() {

        WorkExperience workExperience = new WorkExperience();

        when(workExperienceRepository.findAll()).thenReturn(List.of(workExperience));

        List<WorkExperience> workExperienceList = workExperienceService.getWorkExperienceAll();

        assertNotNull(workExperienceList);
        assertEquals(1, workExperienceList.size());
        verify(workExperienceRepository).findAll();

    }

    @Test
    void getuserWorkExperience_Success() {

        WorkExperience workExperience = new WorkExperience();

        when(workExperienceRepository.findByUserId(1L)).thenReturn(List.of(workExperience));

        List<WorkExperience> workExperienceList = workExperienceService.getuserWorkExperience(1L);

        assertNotNull(workExperienceList);
        assertEquals(1, workExperienceList.size());
        verify(workExperienceRepository).findByUserId(1L);

    }

    @Test
    void getWorkExperienceById_Success() {

        WorkExperience workExperience = new WorkExperience();

        when(workExperienceRepository.findById(1L)).thenReturn(Optional.of(workExperience));

        var workExperienceOptional = workExperienceService.getWorkExperienceById(1L);

        assertTrue(workExperienceOptional.isPresent());
        assertEquals(workExperience, workExperienceOptional.get());
        verify(workExperienceRepository).findById(1L);

    }

    @Test
    void saveWorkExperience_Success() {
        WorkExperienceDto workExperienceDto = new WorkExperienceDto();
        workExperienceDto.setCompanyName("Company");
        workExperienceDto.setPosition("Position");
        workExperienceDto.setStartDate(LocalDate.of(2020, 1, 1));
        workExperienceDto.setEndDate(LocalDate.of(2021, 1, 1));
        workExperienceDto.setDescription("Description");
        workExperienceDto.setUserId(1L);

        UserEntity user = new UserEntity();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        WorkExperience savedWorkExperience = new WorkExperience();
        when(workExperienceRepository.save(any(WorkExperience.class))).thenReturn(savedWorkExperience);

        WorkExperience result = workExperienceService.saveWorkExperience(workExperienceDto);

        assertNotNull(result);
        verify(workExperienceRepository).save(any(WorkExperience.class));
    }

    @Test
    void saveWorkExperience_InvalidDates(){
        WorkExperienceDto workExperienceDto = new WorkExperienceDto();
        workExperienceDto.setStartDate(LocalDate.of(2023, 1, 1));
        workExperienceDto.setEndDate(LocalDate.of(2021, 1, 1));

        assertThrows(IllegalArgumentException.class, () ->
                workExperienceService.saveWorkExperience(workExperienceDto));

    }

    @Test
    void updateWorkExperience_Success() {
        WorkExperienceDto workExperienceDto = new WorkExperienceDto();
        workExperienceDto.setCompanyName("Company");
        workExperienceDto.setPosition("Position");
        workExperienceDto.setStartDate(LocalDate.of(2020, 1, 1));
        workExperienceDto.setEndDate(LocalDate.of(2021, 1, 1));
        workExperienceDto.setDescription("Description");
        workExperienceDto.setUserId(1L);

        WorkExperience workExperience = new WorkExperience();
        when(workExperienceRepository.findById(1L)).thenReturn(Optional.of(workExperience));
        WorkExperience updatedWorkExperience = new WorkExperience();
        when(workExperienceRepository.save(any(WorkExperience.class))).thenReturn(updatedWorkExperience);

        WorkExperience result = workExperienceService.updateWorkExperience(1L, workExperienceDto);

        assertNotNull(result);
        verify(workExperienceRepository).save(any(WorkExperience.class));
    }

    @Test
    void updateWorkExperience_NotFound(){
        WorkExperienceDto workExperienceDto = new WorkExperienceDto();

        when(workExperienceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                workExperienceService.updateWorkExperience(1L, workExperienceDto));
    }

    @Test
    void deleteWorkExperience_Success() {
        WorkExperience workExperience = new WorkExperience();
        when(workExperienceRepository.findById(1L)).thenReturn(Optional.of(workExperience));

        WorkExperience result = workExperienceService.deleteWorkExperience(1L);

        assertNotNull(result);
        verify(workExperienceRepository).delete(workExperience);
    }

    @Test
    void deleteWorExperience_NotFound(){
        when(workExperienceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                workExperienceService.deleteWorkExperience(1L));
    }

    @Test
    void calculateTotalExperience_Success() {
        WorkExperience exp1 = new WorkExperience();
        exp1.setStartDate(LocalDate.of(2019, 1, 1));
        exp1.setEndDate(LocalDate.of(2020, 1, 1));

        WorkExperience exp2 = new WorkExperience();
        exp2.setStartDate(LocalDate.of(2020, 2, 1));
        exp2.setEndDate(LocalDate.of(2021, 2, 1));

        List<WorkExperience> workExperiences = Arrays.asList(exp1, exp2);

        String result = workExperienceService.calculateTotalExperience(workExperiences);

        assertEquals("2 años 0 meses 0 días", result);
    }


}