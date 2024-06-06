package com.example.licenta.dto;

import com.example.licenta.entities.Immobile;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDTO {
    private int propertyId;
    private String title;
    private String description;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate addDate;
    private String country;
    private String city;
    private String street;
    private String number;
    private String type;
    List<ImmobileDTO> immobileList;
}
