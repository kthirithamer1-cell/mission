package com.projectmission.repository;

import com.projectmission.model.Entraineur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntraineurRepository extends JpaRepository<Entraineur, Long> {
    List<Entraineur> findByClub_Id(Long clubId);
    long countByClub_Id(Long clubId);
}
