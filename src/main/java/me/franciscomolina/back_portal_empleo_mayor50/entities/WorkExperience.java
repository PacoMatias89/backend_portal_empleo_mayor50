package me.franciscomolina.back_portal_empleo_mayor50.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "work_experience")
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "company_name")
    private String companyName;

    @Basic
    @Column(name = "position")
    private String position;

    @Basic
    @Column(name = "start_date")
    private LocalDate startDate;

    @Basic
    @Column(name = "end_date")
    private LocalDate endDate;

    @Basic
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-experiences")
    private UserEntity user;

    @Basic
    @Column(name = "created_at")
    private LocalDate createdAt;

    // Campo para almacenar la ruta del archivo subido
    @Basic
    @Column(name = "file_path")
    private String filePath;  // Este es el campo que almacenará la ruta del archivo

    // Agregar manualmente los métodos getStartDate() y getEndDate() si Lombok no los genera automáticamente
    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    // Calculamos la experiencia laboral en años, meses y días
    @Transient
    private String experience;

    public String getExperience() {
        if (startDate != null && endDate != null) {
            Period period = Period.between(startDate, endDate);
            return period.getYears() + " años " + period.getMonths() + " meses " + period.getDays() + " días";
        } else {
            return "No especificada";
        }
    }

    // Método para calcular toda la experiencia del usuario
    public static String calculateTotalExperience(List<WorkExperience> workExperiences) {
        int totalYears = 0;
        int totalMonths = 0;
        int totalDays = 0;

        for (WorkExperience exp : workExperiences) {
            if (exp.getStartDate() != null && exp.getEndDate() != null) { // Aseguramos que las fechas no sean nulas
                Period period = Period.between(exp.getStartDate(), exp.getEndDate());
                totalYears += period.getYears();
                totalMonths += period.getMonths();
                totalDays += period.getDays();
            }
        }

        // Ajustamos los meses y días si es necesario
        if (totalMonths >= 12) {
            totalYears += totalMonths / 12;
            totalMonths %= 12;
        }

        return "Años total de experiencia: " + totalYears + " años " + totalMonths + " meses " + totalDays + " días";
    }
}
