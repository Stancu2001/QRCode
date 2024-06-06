package com.example.licenta.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePropertyDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String description;


    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    @NotBlank
    private String number;

    @NotBlank
    private String phone1;

    @NotBlank
    private String phone2;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String type_property;
}
