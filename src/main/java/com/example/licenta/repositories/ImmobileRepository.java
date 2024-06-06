package com.example.licenta.repositories;

import com.example.licenta.dto.ImmobileDTO;
import com.example.licenta.entities.Immobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImmobileRepository extends JpaRepository<Immobile, Integer> {
    List<Immobile> findByPropertyUserUserId(int userId);

    List<Immobile> findByPropertyCity(String city);
}
