package me.franciscomolina.back_portal_empleo_mayor50.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationCreateDto {

    private Long jobOfferId;
    private Long userId;
    private String status;
}