package com.example.licenta.dto;

import com.example.licenta.entities.Immobile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationUserDTO {
    private int id;
    private String immobileName;

    private int immobileId;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String checkIn;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String checkOut;

    private int totalPrice;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String createdAt;
    private boolean isExpired;
    private boolean isCanceled;
    private String QrCode;
}
