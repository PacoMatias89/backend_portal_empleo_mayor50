package me.franciscomolina.back_portal_empleo_mayor50.repositories;

import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {


    Optional<JobApplication> findByJobOfferIdAndUserId(Long idJojOffer, Long idUser);

    List<JobApplication> findByUserId(Long id);

    List<JobApplication> findByUser(UserEntity user);

    @Query("SELECT COUNT(a) FROM JobApplication a WHERE a.jobOffer.id = :jobOfferId")
    int countApplicationsByJobOfferId(@Param("jobOfferId") Long jobOfferId);




}
