package me.franciscomolina.back_portal_empleo_mayor50.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationUpdateStatusDto {
    @JsonProperty("status")
    private String status;
}
