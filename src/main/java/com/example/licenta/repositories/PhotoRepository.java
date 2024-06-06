package com.example.licenta.repositories;

import com.example.licenta.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo,Integer> {
}
