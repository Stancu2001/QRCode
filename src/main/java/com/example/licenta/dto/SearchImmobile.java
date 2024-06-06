package com.example.licenta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SearchImmobile {
    @NotBlank
    private String city;
    @NotNull
    private LocalDate checkIn;
    @NotNull
    private LocalDate checkOut;
}
