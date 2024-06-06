package com.example.licenta.dto;

import com.example.licenta.entities.Immobile;
import com.example.licenta.entities.Photo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ImmobileResultDTO {
    private int immobileId;
    private int propertyId;
    private String type;
    private String coverPhoto;
    private int persons;
    private double price;
    private String name;
    private String room;
    private List<String> photos;
    private String details;
    public static ImmobileResultDTO fromEntity(Immobile immobile) {
        ImmobileResultDTO dto = new ImmobileResultDTO();
        dto.setImmobileId(immobile.getImmobileId());
        dto.setPropertyId(immobile.getProperty().getPropertyId());
        dto.setCoverPhoto(immobile.getCoverPhoto().getPhotoName());
        dto.setType(immobile.getProperty().getType());
        dto.setPersons(immobile.getPersons());
        dto.setPrice(immobile.getPrice());
        dto.setName(immobile.getName());
        dto.setRoom(immobile.getRooms().getName());
        dto.setPhotos(immobile.getPhotos().stream()
                .map(Photo::getPhotoName)
                .collect(Collectors.toList()));
        dto.setDetails(immobile.getDetails());
        return dto;
    }
}
