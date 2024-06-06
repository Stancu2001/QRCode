package com.example.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerReservation {
    private int immobileId;
    private String immobileName;
    List<ReservationDetailsDTO> reservationDetailsDTOList;

}
