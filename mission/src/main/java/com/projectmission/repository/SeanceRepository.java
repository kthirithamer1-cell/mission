package com.projectmission.repository;

import com.projectmission.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findByClub_Id(Long clubId);
    List<Seance> findByEntraineur_Id(Long entraineurId);
    List<Seance> findByEntraineur_IdAndDateBetween(Long entraineurId, LocalDate from, LocalDate to);
    List<Seance> findByClub_IdAndDateBetween(Long clubId, LocalDate from, LocalDate to);
}