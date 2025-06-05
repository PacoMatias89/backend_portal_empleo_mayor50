package me.franciscomolina.back_portal_empleo_mayor50.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    private String lastnames;
    private String email;
    private LocalDate birthdate;
    private String password;
    private String confirmPassword;
}
