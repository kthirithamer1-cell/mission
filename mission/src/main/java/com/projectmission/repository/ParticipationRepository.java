package com.projectmission.repository;

import com.projectmission.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findByCompetitionId(Long competitionId);
    List<Participation> findByNageurId(Long nageurId);
    List<Participation> findByClubId(Long clubId);
    List<Participation> findByCompetitionIdAndStatut(Long competitionId, String statut);
    List<Participation> findByNageurIdAndCompetitionId(Long nageurId, Long competitionId);
    boolean existsByNageurIdAndCompetitionId(Long nageurId, Long competitionId);
    long countByCompetitionId(Long competitionId);
    void deleteByNageurIdAndCompetitionId(Long nageurId, Long competitionId);

    @Query("SELECT p FROM Participation p WHERE p.competition.id = :compId AND p.nageur.id = :nageurId AND p.statut <> 'ANNULE'")
    Optional<Participation> findActiveInscription(@Param("compId") Long compId, @Param("nageurId") Long nageurId);
}
