package me.franciscomolina.back_portal_empleo_mayor50.repositories;

import me.franciscomolina.back_portal_empleo_mayor50.entities.Company;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobApplication;
import me.franciscomolina.back_portal_empleo_mayor50.entities.JobOffer;

import me.franciscomolina.back_portal_empleo_mayor50.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {


    List<JobOffer> findByCompany(Company company);

    //Buscar el nombre de la empresa por el id de la oferta de trabajo
    @Query("SELECT j.company.companyName FROM JobOffer j WHERE j.id = :id")
    String findNameCompanyByJobOfferId(@Param("id") Long id);


    //Obtener la oferta de empleo por el id de la empresa
    @Query("SELECT j FROM JobOffer j WHERE j.company.id = :id")
    List<JobOffer> findJobOfferByCompanyId(@Param("id") Long id);


}
