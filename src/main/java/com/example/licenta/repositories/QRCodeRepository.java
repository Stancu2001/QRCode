package com.example.licenta.repositories;

import com.example.licenta.entities.QRCodeReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCodeReservation,Integer> {
}
