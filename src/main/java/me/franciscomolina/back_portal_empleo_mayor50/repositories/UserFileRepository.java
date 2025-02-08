package me.franciscomolina.back_portal_empleo_mayor50.repositories;

import me.franciscomolina.back_portal_empleo_mayor50.entities.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFileRepository extends JpaRepository<UserFile, Long> {
}
