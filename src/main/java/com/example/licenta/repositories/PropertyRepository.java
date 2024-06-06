package com.example.licenta.repositories;

import com.example.licenta.dto.PredictiveSuggestionDTO;
import com.example.licenta.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {
    List<Property> findByUserUserId(int userId);
    @Query("SELECT DISTINCT new com.example.licenta.dto.PredictiveSuggestionDTO(p.city) FROM Property p")
    List<PredictiveSuggestionDTO> findDistinctCityDTOs();
}
