package com.example.licenta.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationDTO {

    @NotNull
    private int immobileId;
    @NotNull
    private int totalprice;
    @NotNull
    private LocalDate checkIn;
    @NotNull
    private LocalDate CheckOut;
}
