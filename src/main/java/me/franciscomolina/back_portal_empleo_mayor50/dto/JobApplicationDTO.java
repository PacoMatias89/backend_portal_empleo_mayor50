package me.franciscomolina.back_portal_empleo_mayor50.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDTO {

    private Long id;
    private Long userId;
    private JobOfferDto jobOffer;
}
