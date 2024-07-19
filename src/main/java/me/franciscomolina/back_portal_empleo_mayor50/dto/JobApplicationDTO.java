package me.franciscomolina.back_portal_empleo_mayor50.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDTO {

    private Long jobOfferId;
    private Long userId;
}