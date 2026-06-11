package com.projectmission.repository;

import com.projectmission.model.Nageur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NageurRepository extends JpaRepository<Nageur, Long> {
    List<Nageur> findByClub_Id(Long clubId);
    long countByClub_Id(Long clubId);
    Optional<Nageur> findByEmail(String email);
}