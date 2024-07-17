package me.franciscomolina.back_portal_empleo_mayor50.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferDto {

    private String title;
    private String description;
    private Double salary;
    private String requirements;
    private String location;
    private LocalDate createdAt;
    private Long companyId; // Only the ID of the company, not the entire object

    // Optional: If you want to include a list of applications
    private List<JobApplicationDTO> applications;

}
