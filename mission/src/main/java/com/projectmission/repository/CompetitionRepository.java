package com.projectmission.repository;

import com.projectmission.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    List<Competition> findByStatut(String statut);
    List<Competition> findByType(String type);
    List<Competition> findByNiveau(String niveau);
    List<Competition> findBySaison(String saison);
    List<Competition> findByStatutAndType(String statut, String type);
}