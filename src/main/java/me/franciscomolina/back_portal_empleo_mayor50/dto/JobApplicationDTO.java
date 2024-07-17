package me.franciscomolina.back_portal_empleo_mayor50.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDTO {

    private Long id;
    private String applicantName;
    private String applicantEmail;
    private LocalDate applicationDate;
    private Long jobOfferId; // Only the ID of the job offer, not the entire object
}