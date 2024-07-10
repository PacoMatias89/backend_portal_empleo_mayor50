package me.franciscomolina.back_portal_empleo_mayor50.repositories;

import me.franciscomolina.back_portal_empleo_mayor50.entities.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long>{
    @Query("SELECT we FROM WorkExperience we WHERE we.user.id = :userId")
    List<WorkExperience> findByUserId(Long userId);  // Explicitly specify attribute
}
