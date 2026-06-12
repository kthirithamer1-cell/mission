package com.projectmission.repository;

import com.projectmission.model.Presence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PresenceRepository extends JpaRepository<Presence, Long> {
    List<Presence> findBySeance_Id(Long seanceId);
    Optional<Presence> findBySeance_IdAndNageur_Id(Long seanceId, Long nageurId);
    List<Presence> findByNageur_Id(Long nageurId);
}
