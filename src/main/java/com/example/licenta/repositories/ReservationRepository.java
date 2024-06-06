package com.example.licenta.repositories;

import com.example.licenta.dto.PredictiveSuggestionDTO;
import com.example.licenta.entities.Immobile;
import com.example.licenta.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    @Query("SELECT r FROM Reservation r WHERE r.immobile = :immobile AND " +
            "((:checkIn BETWEEN r.checkIn AND r.checkOut) OR " +
            "(:checkOut BETWEEN r.checkIn AND r.checkOut) OR " +
            "(r.checkIn BETWEEN :checkIn AND :checkOut) OR " +
            "(r.checkOut BETWEEN :checkIn AND :checkOut)) AND " +
            "r.isCanceled = false")
    List<Reservation> findByImmobileAndDateRange(@Param("immobile") Immobile immobile,
                                                 @Param("checkIn") LocalDate checkIn,
                                                 @Param("checkOut") LocalDate checkOut);
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.immobile = :immobile AND " +
            "((:checkIn BETWEEN r.checkIn AND r.checkOut) OR " +
            "(:checkOut BETWEEN r.checkIn AND r.checkOut) OR " +
            "(r.checkIn BETWEEN :checkIn AND :checkOut) OR " +
            "(r.checkOut BETWEEN :checkIn AND :checkOut)) AND " +
            "r.isCanceled = false AND r.isExpired=false" )
    boolean existsReservationInDateRange(@Param("immobile") Immobile immobile,
                                         @Param("checkIn") LocalDate checkIn,
                                         @Param("checkOut") LocalDate checkOut);

    List<Reservation> findByUserUserId(int userID);

    Optional<Reservation> findByQrCodeReservationsKey(UUID key);

}

