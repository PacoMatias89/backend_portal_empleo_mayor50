package me.franciscomolina.back_portal_empleo_mayor50.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import me.franciscomolina.back_portal_empleo_mayor50.entities.WorkExperience;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Period;
import java.util.Collection;
import java.util.List;
import java.util.Set;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntityPrincipal implements UserDetails {

    private Long id;
    private String username;
    transient private String password;
    transient private UserEntity user;
    private Set<GrantedAuthority> authorities;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String calculateTotalExperience(List<WorkExperience> workExperiences) {
        Period totalPeriod = Period.of(0, 0, 0);

        for (WorkExperience exp : workExperiences) {
            if (exp.getStartDate() != null && exp.getEndDate() != null) {
                Period period = Period.between(exp.getStartDate(), exp.getEndDate());
                totalPeriod = totalPeriod.plus(period);
            }
        }

        return totalPeriod.getYears() + " años " + totalPeriod.getMonths() + " meses " + totalPeriod.getDays() + " días";
    }
}
