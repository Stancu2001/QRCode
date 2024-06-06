package com.example.licenta.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ImmobileDetailsDTO {
    private int immobileId;
    private String coverPhoto;
    private int persons;
    private int price;
    private String room;
    private String name;
    private List<String> photos;
    private String details;

    private String title;
    private String description;
    private String country;
    private String city;
    private String street;
    private String number;
    private String type;
    private String phone1;
    private String phone2;
    private String email;
}
