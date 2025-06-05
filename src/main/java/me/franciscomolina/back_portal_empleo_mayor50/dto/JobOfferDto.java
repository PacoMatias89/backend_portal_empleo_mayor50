package me.franciscomolina.back_portal_empleo_mayor50.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferDto {

    private Long id; // Agrega este campo para mantener el ID de la oferta
    private String title;
    private String description;
    private String nameCompany;
    private Double salary;
    private String requirements;
    private String location;
    private LocalDate createdAt;
    private Long companyId; // Solo el ID de la compañía, no todo el objeto

    // Opcional: Si deseas incluir una lista de aplicaciones

}
