package com.example.licenta.dto;

import com.example.licenta.entities.Immobile;
import com.example.licenta.entities.Photo;
import com.example.licenta.entities.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImmobileDTO {
    private int immobileId;
    private String coverPhoto;
    private int persons;
    private int price;
    private String room;
    private String name;
    private List<String> photos;
    private String details;
    public static ImmobileDTO fromEntity(Immobile immobile) {
        ImmobileDTO dto = new ImmobileDTO();
        dto.setImmobileId(immobile.getImmobileId());
        dto.setCoverPhoto(immobile.getCoverPhoto().getPhotoName());
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
