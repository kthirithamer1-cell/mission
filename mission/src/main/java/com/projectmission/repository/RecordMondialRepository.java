package com.projectmission.repository;

import com.projectmission.model.RecordMondial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecordMondialRepository extends JpaRepository<RecordMondial, Long> {
    List<RecordMondial> findByBassinAndSexe(String bassin, String sexe);
}
