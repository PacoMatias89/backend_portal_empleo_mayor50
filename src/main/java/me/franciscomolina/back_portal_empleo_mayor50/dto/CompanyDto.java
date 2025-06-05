package me.franciscomolina.back_portal_empleo_mayor50.dto;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    /*Datos para que la empresa pueda acceder al portal*/

    private String name;

    private String lastname;

    private String email;

    private String password;

    private String confirmPasswordCompany;

    /*Datos de la empresa*/

    private String companyName;

    private String phoneContact;

    private String cifCompany;

    private Boolean isEtt;

    @Basic
    @Column(name="description")
    private String description;

}
