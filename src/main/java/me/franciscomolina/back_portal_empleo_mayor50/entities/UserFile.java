package me.franciscomolina.back_portal_empleo_mayor50.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_files")
public class UserFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Lob // Para almacenar datos binarios
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Evitar referencias cíclicas en la serialización JSON
    private UserEntity user;
}
