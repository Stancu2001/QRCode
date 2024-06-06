package com.example.licenta.service;

import com.example.licenta.dto.CreatePropertyDTO;
import com.example.licenta.dto.PredictiveSuggestionDTO;
import com.example.licenta.dto.PropertyDTO;
import com.example.licenta.entities.Property;

import java.util.List;

public interface PropertyService {
    boolean saveProperty( CreatePropertyDTO createPropertyDTO);

    List<PropertyDTO> getUserProperty();

    boolean updateProperty(int id,CreatePropertyDTO createPropertyDTO);

    boolean deleteProperty(int id);

    void saveProperty(Property property);
    Property findProperty(int id);
    List<PredictiveSuggestionDTO> getDistinctCityDTOs();
}
