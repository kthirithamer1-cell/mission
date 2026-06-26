package com.projectmission.repository;

import com.projectmission.model.Resultat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResultatRepository extends JpaRepository<Resultat, Long> {
    List<Resultat> findByNageurId(Long nageurId);
    List<Resultat> findByEpreuveCompetitionId(Long competitionId);
    List<Resultat> findByRecordTrue();
    List<Resultat> findByNageur_Id(Long nageurId);
}