package me.franciscomolina.back_portal_empleo_mayor50.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperienceDto {

        @NotNull(message = "El nombre de la empresa es obligatorio")
        @NotBlank(message = "El nombre de la empresa no puede estar vacío")
        private String companyName;

        @NotNull(message = "El cargo es obligatorio")
        @NotBlank(message = "El cargo no puede estar vacío")
        private String position;

        @NotNull(message = "La fecha de inicio es obligatoria")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @PastOrPresent(message = "La fecha de inicio no puede ser en el futuro")
        private LocalDate startDate;

        @NotNull(message = "La fecha de fin es obligatoria")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @PastOrPresent(message = "La fecha de fin no puede ser en el futuro")
        private LocalDate endDate;

        @NotNull(message = "La descripción es obligatoria")
        @NotBlank(message = "La descripción no puede estar vacía")
        private String description;

        @NotNull(message = "El id del usuario es obligatorio")
        private Long userId;


        private MultipartFile file;  // Campo para recibir el archivo

}
