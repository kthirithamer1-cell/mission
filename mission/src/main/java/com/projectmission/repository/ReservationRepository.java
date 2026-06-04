package com.projectmission.repository;

import com.projectmission.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClub_Id(Long clubId);
    List<Reservation> findByStatut(String statut);
    List<Reservation> findByClub_IdAndStatut(Long clubId, String statut);

    @Query("SELECT r FROM Reservation r WHERE r.piscine.id = :piscineId AND r.date = :date AND r.statut <> 'ANNULE'")
    List<Reservation> findActiveByPiscineAndDate(@Param("piscineId") Long piscineId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.club.id = :clubId AND r.statut = 'CONFIRME' AND r.date >= :from")
    long countConfirmedFromDate(@Param("clubId") Long clubId, @Param("from") LocalDate from);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.club.id = :clubId AND r.date >= :from AND r.statut <> 'ANNULE'")
    long countUpcomingByClub(@Param("clubId") Long clubId, @Param("from") LocalDate from);
}