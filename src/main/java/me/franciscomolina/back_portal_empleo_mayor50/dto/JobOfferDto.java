package me.franciscomolina.back_portal_empleo_mayor50.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferDto {
    private Long id;
    private String title;
    private String description;
    private Double salary;
    private String requirements;
    private String location;
    private LocalDate createdAt;
    private Long companyId;



}
