package me.franciscomolina.back_portal_empleo_mayor50.repositories;

import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;
import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Optional<JobApplication> findByJobOfferIdAndUserId(Long idJobOffer, Long idUser);

    List<JobApplication> findByUserId(Long id);

    List<JobApplication> findByUser(UserEntity user);

    @Query("SELECT ja FROM JobApplication ja JOIN ja.jobOffer jo WHERE jo.company.id = :companyId")
    List<JobApplication> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(a) FROM JobApplication a WHERE a.jobOffer.id = :jobOfferId")
    int countApplicationsByJobOfferId(@Param("jobOfferId") Long jobOfferId);


    @Query("SELECT COUNT(a) FROM JobApplication a WHERE a.jobOffer.company.id = :companyId")
    int countTotalApplicationsByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT ja FROM JobApplication ja JOIN ja.jobOffer jo WHERE jo.company.id = :companyId AND ja.createdAt BETWEEN :startDate AND :endDate")
    List<JobApplication> findByCompanyIdAndDateRange(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT ja FROM JobApplication ja JOIN ja.user u WHERE ja.jobOffer.id = :jobOfferId")
    List<JobApplication> findUserNamesByJobOfferId(@Param("jobOfferId") Long jobOfferId);



    @Query("SELECT jo FROM JobOffer jo WHERE jo.company.id = :companyId AND EXISTS (" +
            "SELECT ja FROM JobApplication ja WHERE ja.jobOffer.id = jo.id)")
    List<JobOffer> findJobOffersWithApplicantsByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT ja FROM JobApplication ja " +
            "JOIN FETCH ja.jobOffer jo " +
            "JOIN FETCH ja.user u " +
            "WHERE ja.jobOffer.id = :jobOfferId")
    List<JobApplication> findByJobOfferIdWithUser(@Param("jobOfferId") Long jobOfferId);

    @Modifying
    @Query("DELETE FROM JobApplication ja WHERE ja.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM JobApplication ja WHERE ja.user = :user")
    void deleteByUser(UserEntity user);





}
