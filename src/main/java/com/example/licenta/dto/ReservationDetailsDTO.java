package com.example.licenta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetailsDTO {
    private int id;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String checkIn;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String checkOut;
    private String userName;
    private int totalPrice;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String createdAt;
    private boolean isExpired;
    private boolean isCanceled;
}