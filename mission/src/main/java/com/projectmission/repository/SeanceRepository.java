package com.projectmission.repository;

import com.projectmission.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findByClub_Id(Long clubId);
}